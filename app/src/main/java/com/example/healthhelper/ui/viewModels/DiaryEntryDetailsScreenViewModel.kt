package com.example.healthhelper.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthhelper.core.ResultOfRequest
import com.example.healthhelper.data.repository.UserDiaryRepository
import com.example.healthhelper.domain.model.DiaryEntry
import com.example.healthhelper.presenter.DiaryProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiaryEntryDetailsScreenViewModel @Inject constructor(
    private val userDiaryRepository: UserDiaryRepository,
) : ViewModel() {

    private val _diaryEntry = MutableStateFlow(DiaryEntry())
    val diaryEntry = _diaryEntry.asStateFlow()

    private var deletionDiaryEntryJob: Job? = null

    private val _resultOfDeletionDiaryEntry =
        MutableStateFlow<ResultOfRequest<Unit>>(ResultOfRequest.Loading)
    val resultOfDeletionDiaryEntry = _resultOfDeletionDiaryEntry.asStateFlow()

    init {
        viewModelScope.launch {
            DiaryProvider.diaryEntry.collect {
                _diaryEntry.value = it
            }
        }
    }

    fun deleteDiaryEntry() {
        deletionDiaryEntryJob?.cancel()
        deletionDiaryEntryJob = viewModelScope.launch {
            userDiaryRepository.deleteDiaryEntry(diaryEntry.value.index)
            userDiaryRepository.resultOfDeletionDiaryEntry.collect {
                _resultOfDeletionDiaryEntry.value = it
            }
        }
    }
}