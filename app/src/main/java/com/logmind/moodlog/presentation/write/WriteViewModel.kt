package com.logmind.moodlog.presentation.write

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.logmind.moodlog.domain.common.Result
import com.logmind.moodlog.domain.entities.*
import com.logmind.moodlog.domain.usecases.JournalUseCase
import com.logmind.moodlog.domain.usecases.TagUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

data class WriteUiState(
    val selectedMood: MoodType = MoodType.NEUTRAL,
    val content: String = "",
    val imageUris: List<Uri> = emptyList(),
    val availableTags: List<Tag> = emptyList(),
    val selectedTags: Set<Int> = emptySet(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val canSave: Boolean = false,
    val isSaved: Boolean = false
)

@HiltViewModel
class WriteViewModel @Inject constructor(
    private val journalUseCase: JournalUseCase,
    private val tagUseCase: TagUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(WriteUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadTags()
        updateCanSave()
    }

    fun updateMood(moodType: MoodType) {
        _uiState.update { 
            it.copy(selectedMood = moodType)
        }
        updateCanSave()
    }

    fun updateContent(content: String) {
        _uiState.update { 
            it.copy(content = content)
        }
        updateCanSave()
    }

    fun updateImages(images: List<Uri>) {
        _uiState.update { 
            it.copy(imageUris = images)
        }
        updateCanSave()
    }

    fun addImage(uri: Uri) {
        _uiState.update { 
            it.copy(imageUris = it.imageUris + uri)
        }
        updateCanSave()
    }

    fun removeImage(uri: Uri) {
        _uiState.update { 
            it.copy(imageUris = it.imageUris - uri)
        }
        updateCanSave()
    }

    fun toggleTag(tagId: Int) {
        _uiState.update { currentState ->
            val newSelectedTags = if (currentState.selectedTags.contains(tagId)) {
                currentState.selectedTags - tagId
            } else {
                currentState.selectedTags + tagId
            }
            currentState.copy(selectedTags = newSelectedTags)
        }
    }

    fun createNewTag(tagName: String) {
        if (tagName.isBlank()) return
        
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            when (val result = tagUseCase.addTag(tagName, null)) {
                is Result.Success -> {
                    loadTags()
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            errorMessage = result.exception.message
                        )
                    }
                }
            }
        }
    }

    fun saveJournal() {
        val currentState = _uiState.value
        
        if (!isValidForSaving(currentState)) {
            _uiState.update { 
                it.copy(errorMessage = "내용을 입력해주세요.")
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            val dto = CreateJournalDto(
                content = currentState.content.takeIf { it.isNotBlank() },
                moodType = currentState.selectedMood,
                imageUris = currentState.imageUris.map { it.toString() }.takeIf { it.isNotEmpty() },
                aiResponseEnabled = false, // TODO: settings에서 가져오기
                aiResponse = null,
                createdAt = LocalDateTime.now(),
                latitude = null, // TODO: 위치 서비스 연동
                longitude = null,
                address = null,
                temperature = null, // TODO: 날씨 서비스 연동
                weatherIcon = null,
                weatherDescription = null
            )

            when (val result = journalUseCase.addJournal(dto)) {
                is Result.Success -> {
                    val journalId = (result.data["journalId"] as? Number)?.toInt()
                    
                    if (journalId != null && currentState.selectedTags.isNotEmpty()) {
                        when (val tagResult = tagUseCase.updateJournalTags(
                            journalId = journalId,
                            tagIds = currentState.selectedTags.toList()
                        )) {
                            is Result.Success -> {
                                _uiState.update { 
                                    it.copy(
                                        isLoading = false,
                                        errorMessage = null,
                                        isSaved = true
                                    )
                                }
                            }
                            is Result.Error -> {
                                _uiState.update { 
                                    it.copy(
                                        isLoading = false,
                                        errorMessage = "일기는 저장되었으나 태그 연결에 실패했습니다: ${tagResult.exception.message}"
                                    )
                                }
                            }
                        }
                    } else {
                        _uiState.update { 
                            it.copy(
                                isLoading = false,
                                errorMessage = null,
                                isSaved = true
                            )
                        }
                    }
                }
                is Result.Error -> {
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            errorMessage = result.exception.message
                        )
                    }
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    private fun loadTags() {
        viewModelScope.launch {
            when (val result = tagUseCase.getAllTags()) {
                is Result.Success -> {
                    _uiState.update { 
                        it.copy(availableTags = result.data)
                    }
                }
                is Result.Error -> {
                    _uiState.update { 
                        it.copy(errorMessage = result.exception.message)
                    }
                }
            }
        }
    }


    private fun updateCanSave() {
        val currentState = _uiState.value
        _uiState.update { 
            it.copy(canSave = isValidForSaving(currentState))
        }
    }

    private fun isValidForSaving(state: WriteUiState): Boolean {
        return state.content.isNotBlank() || state.imageUris.isNotEmpty()
    }
}