package com.logmind.moodlog.domain.repositories

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val userChanges: Flow<FirebaseUser?>
    val isAuthenticated: Boolean
    val isAnonymousUser: Boolean
    suspend fun getCurrentUser(): FirebaseUser?
    suspend fun signInAnonymously(): FirebaseUser?
    suspend fun updateDisplayName(displayName: String)
    suspend fun signInWithGoogle(): FirebaseUser?
    suspend fun signInWithGoogleCredential(idToken: String): FirebaseUser?
    suspend fun linkWithCredential(): FirebaseUser?
    suspend fun signOut()
    suspend fun updateProfileImage(profileImage: String)
}