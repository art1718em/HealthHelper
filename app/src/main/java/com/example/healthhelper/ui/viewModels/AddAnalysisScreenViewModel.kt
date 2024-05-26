package com.example.healthhelper.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthhelper.core.ResultOfRequest
import com.example.healthhelper.data.api.UserAnalyzesApi
import com.example.healthhelper.ui.screens.main.addAnalysis.AddAnalysisScreenUiState
import com.example.healthhelper.utils.toFormattedDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AddAnalysisScreenViewModel @Inject constructor(
    private val userAnalyzesApi: UserAnalyzesApi,
) : ViewModel() {

    private val _addAnalysisScreenUiState = MutableStateFlow(AddAnalysisScreenUiState())
    val addAnalysisScreenUiState = _addAnalysisScreenUiState.asStateFlow()

    private var addingAnalysisJob: Job? = null

    private val _resultOfAddingAnalysis =
        MutableStateFlow<ResultOfRequest<Unit>>(ResultOfRequest.Loading)
    val resultOfAddingAnalysis = _resultOfAddingAnalysis.asStateFlow()

    fun updateName(name: String) {
        _addAnalysisScreenUiState.value = addAnalysisScreenUiState.value.copy(
            name = name,
        )
    }

    fun updateUnit(unit: String) {
        _addAnalysisScreenUiState.value = addAnalysisScreenUiState.value.copy(
            unit = unit,
        )
    }

    fun updateResult(result: String) {
        _addAnalysisScreenUiState.value = addAnalysisScreenUiState.value.copy(
            result = result,
        )
    }

    fun updateLowerLimit(lowerLimit: String) {
        _addAnalysisScreenUiState.value = addAnalysisScreenUiState.value.copy(
            lowerLimit = lowerLimit,
        )
    }

    fun updateUpperLimit(upperLimit: String) {
        _addAnalysisScreenUiState.value = addAnalysisScreenUiState.value.copy(
            upperLimit = upperLimit,
        )
    }

    fun updateDate(date: LocalDate) {
        _addAnalysisScreenUiState.value = addAnalysisScreenUiState.value.copy(
            pickedDate = date,
            formattedDate = date.toFormattedDate(),
        )
    }

    fun addAnalysis() {
        addingAnalysisJob?.cancel()
        val analysis = addAnalysisScreenUiState.value.toAnalysis()
        addingAnalysisJob = viewModelScope.launch {
            _resultOfAddingAnalysis.value = userAnalyzesApi.addAnalysis(analysis)
        }
    }
}


