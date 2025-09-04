package com.logmind.moodlog.domain.usecases

import com.google.firebase.auth.FirebaseUser
import com.logmind.moodlog.domain.repositories.AuthRepository
import javax.inject.Inject

class AuthUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    suspend fun signInWithGoogle(): Result<FirebaseUser?> {
        return runCatching { authRepository.signInWithGoogle() }
    }

    suspend fun signInAnonymously(): Result<FirebaseUser?> {
        return runCatching { authRepository.signInAnonymously() }
    }

    suspend fun signInWithGoogleCredential(idToken: String): Result<FirebaseUser?> {
        return runCatching { authRepository.signInWithGoogleCredential(idToken) }
    }

    suspend fun signOut() {
        authRepository.signOut()
    }

    suspend fun updateDisplayName(displayName: String): Result<Unit> {
        return runCatching { authRepository.updateDisplayName(displayName) }
    }

    suspend fun updateProfileImage(photoURL: String): Result<Unit> {
        return runCatching { authRepository.updateProfileImage(photoURL) }
    }
}