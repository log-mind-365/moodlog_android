package com.logmind.moodlog.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.logmind.moodlog.domain.usecases.AuthUseCase
import com.logmind.moodlog.domain.usecases.UserChangesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val user: FirebaseUser? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSignedIn: Boolean = false
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val userChangesUseCase: UserChangesUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        observeAuthState()
    }

    private fun observeAuthState() {
        viewModelScope.launch {
            userChangesUseCase().collect { user ->
                _uiState.update {
                    it.copy(
                        user = user,
                        isSignedIn = user != null,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun signInAnonymously() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    error = null
                )
            }

            authUseCase.signInAnonymously()
                .onSuccess { result ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            user = result,
                            isSignedIn = true
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error.message
                        )
                    }
                }
        }
    }

    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    error = null
                )
            }

            authUseCase.signInWithGoogleCredential(idToken)
                .onSuccess { result ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            user = result,
                            isSignedIn = true
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error.message
                        )
                    }
                }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            authUseCase.signOut()
            _uiState.update {
                it.copy(
                    user = null,
                    isSignedIn = false,
                    error = null
                )
            }
        }
    }

    fun clearError() {
        _uiState.update {
            it.copy(error = null)
        }
    }
}