package com.example.healthhelper.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthhelper.R
import com.example.healthhelper.core.ResultOfRequest
import com.example.healthhelper.data.repository.UserDiaryRepository
import com.example.healthhelper.ui.screens.main.addDiaryEntry.AddDiaryEntryScreenUiState
import com.example.healthhelper.utils.toFormattedDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AddDiaryEntryScreenViewModel @Inject constructor(
    private val userDiaryRepository: UserDiaryRepository,
) : ViewModel() {

    private val _addDiaryEntryScreenUiState = MutableStateFlow(AddDiaryEntryScreenUiState())
    val addDiaryEntryScreenUiState = _addDiaryEntryScreenUiState.asStateFlow()

    private var addingDiaryEntryJob: Job? = null

    private val _resultOfAddingDiaryEntry =
        MutableStateFlow<ResultOfRequest<Unit>>(ResultOfRequest.Loading)
    val resultOfAddingDiaryEntry = _resultOfAddingDiaryEntry.asStateFlow()

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

    fun addDiaryEntry(index: Int) {
        addingDiaryEntryJob?.cancel()
        val diaryEntry = addDiaryEntryScreenUiState.value.toDiaryEntry().copy(
            index = index,
        )
        addingDiaryEntryJob = viewModelScope.launch {
            userDiaryRepository.addDiaryEntry(diaryEntry)
            userDiaryRepository.resultOfAddingDiaryEntry.collect {
                _resultOfAddingDiaryEntry.value = it
            }
        }
    }
}