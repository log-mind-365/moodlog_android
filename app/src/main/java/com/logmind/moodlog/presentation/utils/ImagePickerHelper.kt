package com.logmind.moodlog.presentation.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ImagePickerHelper {

    fun createImageFile(context: Context): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File = File(context.getExternalFilesDir(null), "Pictures")
        if (!storageDir.exists()) {
            storageDir.mkdirs()
        }
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        )
    }

    fun getImageUri(context: Context, file: File): Uri {
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    }

    suspend fun resizeImage(
        context: Context,
        uri: Uri,
        maxWidth: Int = 1024,
        maxHeight: Int = 1024,
        quality: Int = 85
    ): Uri? = withContext(Dispatchers.IO) {
        try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val originalBitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()

            if (originalBitmap == null) return@withContext null

            // Get orientation
            val orientation = getImageOrientation(context, uri)
            val rotatedBitmap = rotateImageIfRequired(originalBitmap, orientation)

            // Calculate new dimensions
            val aspectRatio = rotatedBitmap.width.toFloat() / rotatedBitmap.height.toFloat()
            val (newWidth, newHeight) = if (aspectRatio > 1) {
                // Landscape
                val width = minOf(maxWidth, rotatedBitmap.width)
                val height = (width / aspectRatio).toInt()
                width to height
            } else {
                // Portrait or square
                val height = minOf(maxHeight, rotatedBitmap.height)
                val width = (height * aspectRatio).toInt()
                width to height
            }

            // Resize bitmap
            val resizedBitmap = if (newWidth != rotatedBitmap.width || newHeight != rotatedBitmap.height) {
                Bitmap.createScaledBitmap(rotatedBitmap, newWidth, newHeight, true)
            } else {
                rotatedBitmap
            }

            // Save to file
            val outputFile = createImageFile(context)
            val outputStream = FileOutputStream(outputFile)
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
            outputStream.close()

            // Clean up bitmaps
            if (originalBitmap != rotatedBitmap) originalBitmap.recycle()
            if (rotatedBitmap != resizedBitmap) rotatedBitmap.recycle()
            resizedBitmap.recycle()

            getImageUri(context, outputFile)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun getImageOrientation(context: Context, uri: Uri): Int {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val exif = inputStream?.let { ExifInterface(it) }
            inputStream?.close()
            
            when (exif?.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90
                ExifInterface.ORIENTATION_ROTATE_180 -> 180
                ExifInterface.ORIENTATION_ROTATE_270 -> 270
                else -> 0
            }
        } catch (e: IOException) {
            0
        }
    }

    private fun rotateImageIfRequired(bitmap: Bitmap, degrees: Int): Bitmap {
        return if (degrees != 0) {
            val matrix = Matrix().apply { postRotate(degrees.toFloat()) }
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        } else {
            bitmap
        }
    }
}