package com.logmind.moodlog.presentation.profile

import androidx.lifecycle.ViewModel
import com.logmind.moodlog.domain.repositories.AuthRepository
import javax.inject.Inject

class ProfileViewModel @Inject constructor(private val authRepository: AuthRepository) :
    ViewModel() {

}