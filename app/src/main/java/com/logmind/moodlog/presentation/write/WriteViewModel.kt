package com.logmind.moodlog.presentation.write

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.logmind.moodlog.domain.entities.CreateJournalDto
import com.logmind.moodlog.domain.entities.MoodType
import com.logmind.moodlog.domain.entities.Tag
import com.logmind.moodlog.domain.usecases.JournalUseCase
import com.logmind.moodlog.domain.usecases.TagUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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

            tagUseCase.addTag(tagName, null)
                .onSuccess {
                    loadTags()
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message
                        )
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

            journalUseCase.addJournal(dto)
                .onSuccess { journal ->
                    val journalId = (journal["journalId"] as? Number)?.toInt()

                    if (journalId != null && currentState.selectedTags.isNotEmpty()) {
                        tagUseCase.updateJournalTags(
                            journalId = journalId,
                            tagIds = currentState.selectedTags.toList()
                        )
                            .onSuccess {
                                _uiState.update {
                                    it.copy(
                                        isLoading = false,
                                        errorMessage = null,
                                        isSaved = true
                                    )
                                }
                            }
                            .onFailure { error ->
                                _uiState.update {
                                    it.copy(
                                        isLoading = false,
                                        errorMessage = "일기는 저장되었으나 태그 연결에 실패했습니다: ${error.message}"
                                    )
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
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message
                        )
                    }
                }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    private fun loadTags() {
        viewModelScope.launch {
            tagUseCase.getAllTags()
                .onSuccess { result ->
                    _uiState.update {
                        it.copy(availableTags = it.availableTags + result)
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(errorMessage = error.message)
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