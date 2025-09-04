package com.logmind.moodlog.domain.usecases

import com.google.firebase.auth.FirebaseUser
import com.logmind.moodlog.domain.repositories.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserChangesUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): Flow<FirebaseUser?> = authRepository.userChanges
}