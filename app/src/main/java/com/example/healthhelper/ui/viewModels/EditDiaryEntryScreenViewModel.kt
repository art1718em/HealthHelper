package com.example.healthhelper.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthhelper.R
import com.example.healthhelper.core.ResultOfRequest
import com.example.healthhelper.data.repository.UserDiaryRepository
import com.example.healthhelper.domain.model.DiaryEntry
import com.example.healthhelper.presenter.DiaryProvider
import com.example.healthhelper.ui.screens.main.addDiaryEntry.AddDiaryEntryScreenUiState
import com.example.healthhelper.utils.toFormattedDate
import com.example.healthhelper.utils.toLocalDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class EditDiaryEntryScreenViewModel @Inject constructor(
    private val userDiaryRepository: UserDiaryRepository,
) : ViewModel() {

    private val _addDiaryEntryScreenUiState = MutableStateFlow(AddDiaryEntryScreenUiState())
    val addDiaryEntryScreenUiState = _addDiaryEntryScreenUiState.asStateFlow()

    private var editingDiaryEntryJob: Job? = null

    private val _diaryEntry = MutableStateFlow(DiaryEntry())
    private val diaryEntry = _diaryEntry.asStateFlow()

    private val _resultOfEditingDiaryEntry =
        MutableStateFlow<ResultOfRequest<Unit>>(ResultOfRequest.Loading)
    val resultOfEditingDiaryEntry = _resultOfEditingDiaryEntry.asStateFlow()

    init {
        viewModelScope.launch {
            DiaryProvider.diaryEntry.collect {
                _diaryEntry.value = it
                _addDiaryEntryScreenUiState.value = AddDiaryEntryScreenUiState(
                    heading = diaryEntry.value.heading,
                    description = diaryEntry.value.description,
                    pickedDate = diaryEntry.value.date.toLocalDate(),
                    formattedDate = diaryEntry.value.date,
                    headingErrorMessage = null,
                    descriptionErrorMessage = null,
                )
            }
        }
    }

    fun updateHeading(heading: String) {
        _addDiaryEntryScreenUiState.value = addDiaryEntryScreenUiState.value.copy(
            heading = heading,
        )
        checkHeading()
    }

    fun updateDescription(description: String) {
        _addDiaryEntryScreenUiState.value = addDiaryEntryScreenUiState.value.copy(
            description = description,
        )
        checkDescription()
    }

    fun updateDate(date: LocalDate) {
        _addDiaryEntryScreenUiState.value = addDiaryEntryScreenUiState.value.copy(
            pickedDate = date,
            formattedDate = date.toFormattedDate(),
        )
    }

    private fun checkHeading() {
        _addDiaryEntryScreenUiState.value = addDiaryEntryScreenUiState.value.copy(
            headingErrorMessage = when {
                addDiaryEntryScreenUiState.value.heading.isEmpty() -> R.string.empty_field
                else -> null
            }
        )
    }

    private fun checkDescription() {
        _addDiaryEntryScreenUiState.value = addDiaryEntryScreenUiState.value.copy(
            descriptionErrorMessage = when {
                addDiaryEntryScreenUiState.value.description.isEmpty() -> R.string.empty_field
                else -> null
            }
        )
    }

    fun editDiaryEntry() {
        editingDiaryEntryJob?.cancel()
        val updatedDiaryEntry = addDiaryEntryScreenUiState.value.toDiaryEntry().copy(
            index = diaryEntry.value.index,
        )
        editingDiaryEntryJob = viewModelScope.launch {
            userDiaryRepository.editDiaryEntry(updatedDiaryEntry)
            userDiaryRepository.resultOfEditingDiaryEntry.collect {
                _resultOfEditingDiaryEntry.value = it
                if (it is ResultOfRequest.Success) {
                    DiaryProvider.setDiaryEntry(updatedDiaryEntry)
                }
            }
        }
    }
}