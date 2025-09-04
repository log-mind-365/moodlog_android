package com.logmind.moodlog.data.repositories

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.logmind.moodlog.domain.repositories.AuthRepository
import com.logmind.moodlog.utils.execute
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

    override suspend fun getCurrentUser(): FirebaseUser? {
        return execute("getCurrentUser") {
            firebaseAuth.currentUser
        }
    }

    override suspend fun signInAnonymously(): FirebaseUser? {
        return execute("signInAnonymously") {
            val result = firebaseAuth.signInAnonymously().await()
            result.user
        }
    }

    override suspend fun updateDisplayName(displayName: String) {
        return execute("updateDisplayName") {
            val user =
                firebaseAuth.currentUser ?: throw Exception("No current user")
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(displayName)
                .build()
            user.updateProfile(profileUpdates)
        }
    }

    override suspend fun signInWithGoogle(): FirebaseUser? {
        TODO("Not yet implemented")
    }

    override suspend fun signInWithGoogleCredential(idToken: String): FirebaseUser? {
        return execute("signInWithGoogleCredential") {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val result = firebaseAuth.signInWithCredential(credential).await()
            result.user
        }
    }

    override suspend fun linkWithCredential(): FirebaseUser? {
        TODO("익명 계정을 Google 계정과 연결하는 로직, UI에서 Google 토큰을 받아와서 연결해야 함")
    }

    override suspend fun signOut() {
        execute("signOut") {
            firebaseAuth.signOut()
        }
    }

    override suspend fun updateProfileImage(profileImage: String) {
        execute("updateProfileImage") {
            val user =
                firebaseAuth.currentUser ?: throw Exception("No current user")
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setPhotoUri(android.net.Uri.parse(profileImage))
                .build()
            user.updateProfile(profileUpdates).await()
        }
    }
}