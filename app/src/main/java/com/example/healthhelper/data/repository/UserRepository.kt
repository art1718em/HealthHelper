package com.example.healthhelper.data.repository

import com.example.healthhelper.core.ResultOfRequest
import com.example.healthhelper.data.api.UserAnalyzesApi
import com.example.healthhelper.data.api.UserApi
import com.example.healthhelper.data.api.UserDiaryApi
import com.example.healthhelper.domain.model.Analysis
import com.example.healthhelper.domain.model.DiaryEntry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userApi: UserApi,
    private val userDiaryApi: UserDiaryApi,
    private val userAnalyzesApi: UserAnalyzesApi,
) {
    private val _analyzes = MutableStateFlow<List<Analysis>>(emptyList())
    val analyzes = _analyzes.asStateFlow()

    private val _diaryEntries = MutableStateFlow<List<DiaryEntry>>(emptyList())
    val diaryEntries = _diaryEntries.asStateFlow()

    private val _resultOfLoadingData =
        MutableStateFlow<ResultOfRequest<Unit>>(ResultOfRequest.Loading)
    val resultOfLoadingData = _resultOfLoadingData.asStateFlow()

    private val _resultOfAddingAnalysis =
        MutableStateFlow<ResultOfRequest<Unit>>(ResultOfRequest.Loading)
    val resultOfAddingAnalysis = _resultOfAddingAnalysis.asStateFlow()

    private val _resultOfAddingDiaryEntry =
        MutableStateFlow<ResultOfRequest<Unit>>(ResultOfRequest.Loading)
    val resultOfAddingDiaryEntry = _resultOfAddingDiaryEntry.asStateFlow()

    private val coroutineScope = CoroutineScope(Dispatchers.IO + Job())


    suspend fun loadUserData() {
        coroutineScope.launch {
            when (val resultOfLoadData = userApi.loadUserData()) {
                is ResultOfRequest.Success -> {
                    _analyzes.value = resultOfLoadData.result.analyzes
                    _diaryEntries.value = resultOfLoadData.result.diaryEntries
                    _resultOfLoadingData.value = ResultOfRequest.Success(Unit)
                }

                is ResultOfRequest.Error -> {
                    _resultOfLoadingData.value =
                        ResultOfRequest.Error(resultOfLoadData.errorMessage)
                }

                is ResultOfRequest.Loading -> {}
            }
        }
    }

    suspend fun addAnalysis(analysis: Analysis) {
        coroutineScope.launch {
            when (val resultOfAddingAnalysis = userAnalyzesApi.addAnalysis(analysis)) {
                is ResultOfRequest.Success -> {
                    _analyzes.value = analyzes.value.plus(analysis)
                    _resultOfAddingAnalysis.value = ResultOfRequest.Success(Unit)
                }

                is ResultOfRequest.Error -> {
                    _resultOfAddingAnalysis.value =
                        ResultOfRequest.Error(resultOfAddingAnalysis.errorMessage)
                }

                is ResultOfRequest.Loading -> {}
            }
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

    fun clearData() {
        _analyzes.value = emptyList()
        _diaryEntries.value = emptyList()
    }

}