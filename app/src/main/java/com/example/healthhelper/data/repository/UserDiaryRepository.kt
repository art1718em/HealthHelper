package com.example.healthhelper.data.repository

import com.example.healthhelper.core.ResultOfRequest
import com.example.healthhelper.data.api.UserDiaryApi
import com.example.healthhelper.domain.model.DiaryEntry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserDiaryRepository @Inject constructor(
    private val userDiaryApi: UserDiaryApi,
) {

    private val coroutineScope = CoroutineScope(Dispatchers.IO + Job())

    private val _diaryEntries = MutableStateFlow<List<DiaryEntry>>(emptyList())
    val diaryEntries = _diaryEntries.asStateFlow()

    private val _resultOfLoadingDiaryEntries =
        MutableStateFlow<ResultOfRequest<Unit>>(ResultOfRequest.Loading)
    val resultOfLoadingDiaryEntries = _resultOfLoadingDiaryEntries.asStateFlow()

    private val _resultOfAddingDiaryEntry =
        MutableStateFlow<ResultOfRequest<Unit>>(ResultOfRequest.Loading)
    val resultOfAddingDiaryEntry = _resultOfAddingDiaryEntry.asStateFlow()

    private val _resultOfEditingDiaryEntry =
        MutableStateFlow<ResultOfRequest<Unit>>(ResultOfRequest.Loading)
    val resultOfEditingDiaryEntry = _resultOfEditingDiaryEntry.asStateFlow()

    private val _resultOfDeletionDiaryEntry =
        MutableStateFlow<ResultOfRequest<Unit>>(ResultOfRequest.Loading)
    val resultOfDeletionDiaryEntry = _resultOfDeletionDiaryEntry.asStateFlow()

    suspend fun loadUserDiaryEntries() {
        when (val resultOfLoadData = userDiaryApi.loadUserDiaryEntries()) {
            is ResultOfRequest.Success -> {
                _diaryEntries.value = resultOfLoadData.result
                _resultOfLoadingDiaryEntries.value = ResultOfRequest.Success(Unit)
            }

            is ResultOfRequest.Error -> {
                _resultOfLoadingDiaryEntries.value =
                    ResultOfRequest.Error(resultOfLoadData.errorMessage)
            }

            is ResultOfRequest.Loading -> {}
        }
    }

    suspend fun addDiaryEntry(diaryEntry: DiaryEntry) {
        coroutineScope.launch {
            when (val resultOfAddingDiaryEntry = userDiaryApi.addDiaryEntry(diaryEntry)) {
                is ResultOfRequest.Success -> {
                    _diaryEntries.value = diaryEntries.value.plus(diaryEntry)
                    _resultOfAddingDiaryEntry.value = ResultOfRequest.Success(Unit)
                }

                is ResultOfRequest.Error -> {
                    _resultOfAddingDiaryEntry.value =
                        ResultOfRequest.Error(resultOfAddingDiaryEntry.errorMessage)
                }

                is ResultOfRequest.Loading -> {}
            }
        }
    }

    suspend fun editDiaryEntry(diaryEntry: DiaryEntry) {
        coroutineScope.launch {
            when (val resultOfEditingDiaryEntry = userDiaryApi.editDiaryEntry(diaryEntry)) {
                is ResultOfRequest.Success -> {
                    val updatedDiaryEntries = _diaryEntries.value.toMutableList()
                    updatedDiaryEntries[diaryEntry.index] = diaryEntry
                    _diaryEntries.value = updatedDiaryEntries
                    _resultOfEditingDiaryEntry.value = ResultOfRequest.Success(Unit)
                }

                is ResultOfRequest.Error -> {
                    _resultOfEditingDiaryEntry.value =
                        ResultOfRequest.Error(resultOfEditingDiaryEntry.errorMessage)
                }

                is ResultOfRequest.Loading -> {}
            }
        }
    }

    suspend fun deleteDiaryEntry(index: Int) {
        coroutineScope.launch {
            when (val resultOfDeletionDiaryEntry = userDiaryApi.deleteDiaryEntry(index)) {
                is ResultOfRequest.Success -> {
                    val updatedDiaryEntries = _diaryEntries.value.toMutableList()
                    updatedDiaryEntries.removeAt(index)
                    for (i in index..updatedDiaryEntries.lastIndex) {
                        updatedDiaryEntries[i] = updatedDiaryEntries[i].copy(
                            index = updatedDiaryEntries[i].index - 1
                        )
                    }
                    _diaryEntries.value = updatedDiaryEntries
                    _resultOfDeletionDiaryEntry.value = ResultOfRequest.Success(Unit)
                }

                is ResultOfRequest.Error -> {
                    _resultOfDeletionDiaryEntry.value =
                        ResultOfRequest.Error(resultOfDeletionDiaryEntry.errorMessage)
                }

                is ResultOfRequest.Loading -> {}
            }
        }
    }

    fun clearData() {
        _diaryEntries.value = emptyList()
    }

}