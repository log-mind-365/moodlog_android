package com.logmind.moodlog.domain.repositories

import com.google.firebase.auth.FirebaseUser
import com.logmind.moodlog.domain.common.Result
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val userChanges: Flow<FirebaseUser?>
    
    val isAuthenticated: Boolean
    
    val isAnonymousUser: Boolean

    suspend fun getCurrentUser(): Result<FirebaseUser?>

    suspend fun signInAnonymously(): Result<FirebaseUser?>

    suspend fun updateDisplayName(displayName: String): Result<Unit>

    suspend fun signInWithGoogle(): Result<FirebaseUser?>

    suspend fun linkWithCredential(): Result<FirebaseUser?>

    suspend fun signOut()

    suspend fun updateProfileImage(profileImage: String): Result<Unit>
}