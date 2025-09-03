package com.logmind.moodlog.data.repositories

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.logmind.moodlog.domain.common.Result
import com.logmind.moodlog.domain.repositories.AuthRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override val userChanges: Flow<FirebaseUser?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser)
        }
        firebaseAuth.addAuthStateListener(listener)
        awaitClose { firebaseAuth.removeAuthStateListener(listener) }
    }

    override val isAuthenticated: Boolean
        get() = firebaseAuth.currentUser != null

    override val isAnonymousUser: Boolean
        get() = firebaseAuth.currentUser?.isAnonymous == true

    override suspend fun getCurrentUser(): Result<FirebaseUser?> {
        return try {
            Result.Success(firebaseAuth.currentUser)
        } catch (e: Exception) {
            Result.Error(Exception("Failed to get current user: ${e.message}"))
        }
    }

    override suspend fun signInAnonymously(): Result<FirebaseUser?> {
        return try {
            val result = firebaseAuth.signInAnonymously().await()
            Result.Success(result.user)
        } catch (e: Exception) {
            Result.Error(Exception("Anonymous sign-in failed: ${e.message}"))
        }
    }

    override suspend fun updateDisplayName(displayName: String): Result<Unit> {
        return try {
            val user = firebaseAuth.currentUser ?: return Result.Error(Exception("No current user"))
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(displayName)
                .build()
            user.updateProfile(profileUpdates).await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(Exception("Failed to update display name: ${e.message}"))
        }
    }

    override suspend fun signInWithGoogle(): Result<FirebaseUser?> {
        return try {
            // Google Sign-In을 위한 추가 구현 필요 - UI에서 Google 로그인 토큰을 받아와야 함
            // 이 메서드는 Google 토큰을 받은 후에 호출되는 방식으로 수정 예정
            Result.Error(Exception("Google Sign-In requires UI implementation"))
        } catch (e: Exception) {
            Result.Error(Exception("Google sign-in failed: ${e.message}"))
        }
    }

    override suspend fun signInWithGoogleCredential(idToken: String): Result<FirebaseUser?> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val result = firebaseAuth.signInWithCredential(credential).await()
            Result.Success(result.user)
        } catch (e: Exception) {
            Result.Error(Exception("Google sign-in with credential failed: ${e.message}"))
        }
    }

    override suspend fun linkWithCredential(): Result<FirebaseUser?> {
        return try {
            // 익명 계정을 Google 계정과 연결하는 로직
            // UI에서 Google 토큰을 받아와서 연결해야 함
            Result.Error(Exception("Link with credential requires implementation"))
        } catch (e: Exception) {
            Result.Error(Exception("Failed to link with credential: ${e.message}"))
        }
    }

    override suspend fun signOut() {
        try {
            firebaseAuth.signOut()
        } catch (e: Exception) {
            // 로그아웃 실패는 일반적으로 심각하지 않으므로 로그만 남김
            println("Sign out failed: ${e.message}")
        }
    }

    override suspend fun updateProfileImage(profileImage: String): Result<Unit> {
        return try {
            val user = firebaseAuth.currentUser ?: return Result.Error(Exception("No current user"))
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setPhotoUri(android.net.Uri.parse(profileImage))
                .build()
            user.updateProfile(profileUpdates).await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(Exception("Failed to update profile image: ${e.message}"))
        }
    }
}