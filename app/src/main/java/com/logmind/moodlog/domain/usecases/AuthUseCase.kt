package com.logmind.moodlog.domain.usecases

import com.google.firebase.auth.FirebaseUser
import com.logmind.moodlog.domain.common.Result
import com.logmind.moodlog.domain.repositories.AuthRepository
import javax.inject.Inject

class AuthUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend fun signInWithGoogle(): Result<FirebaseUser?> {
        return authRepository.signInWithGoogle()
    }

    suspend fun signOut() {
        authRepository.signOut()
    }

    suspend fun updateDisplayName(displayName: String): Result<Unit> {
        return authRepository.updateDisplayName(displayName)
    }

    suspend fun updateProfileImage(photoURL: String): Result<Unit> {
        return authRepository.updateProfileImage(photoURL)
    }
}