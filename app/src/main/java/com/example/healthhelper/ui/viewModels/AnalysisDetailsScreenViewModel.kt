package com.example.healthhelper.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthhelper.core.ResultOfRequest
import com.example.healthhelper.data.repository.UserAnalyzesRepository
import com.example.healthhelper.domain.model.Analysis
import com.example.healthhelper.presenter.AnalyzesProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnalysisDetailsScreenViewModel @Inject constructor(
    private val userAnalyzesRepository: UserAnalyzesRepository,
) : ViewModel() {

    private val _analysis = MutableStateFlow(Analysis())
    val analysis = _analysis.asStateFlow()

    private var deletionAnalysisJob: Job? = null

    private val _resultOfDeletionAnalysis =
        MutableStateFlow<ResultOfRequest<Unit>>(ResultOfRequest.Loading)
    val resultOfDeletionAnalysis = _resultOfDeletionAnalysis.asStateFlow()

    init {
        viewModelScope.launch {
            AnalyzesProvider.analysis.collect {
                _analysis.value = it
            }
        }
    }

    fun deleteAnalysis() {
        deletionAnalysisJob?.cancel()
        deletionAnalysisJob = viewModelScope.launch {
            userAnalyzesRepository.deleteAnalysis(analysis.value.index)
            userAnalyzesRepository.resultOfDeletionAnalysis.collect {
                _resultOfDeletionAnalysis.value = it
            }
        }
    }
}