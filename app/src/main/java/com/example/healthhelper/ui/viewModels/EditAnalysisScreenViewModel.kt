package com.example.healthhelper.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthhelper.R
import com.example.healthhelper.core.ResultOfRequest
import com.example.healthhelper.data.repository.UserRepository
import com.example.healthhelper.domain.model.Analysis
import com.example.healthhelper.presenter.AnalyzesPresenter
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
class EditAnalysisScreenViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _addAnalysisScreenUiState = MutableStateFlow(AddAnalysisScreenUiState())
    val addAnalysisScreenUiState = _addAnalysisScreenUiState.asStateFlow()

    private var editingAnalysisJob: Job? = null

    private val _analysis = MutableStateFlow(Analysis())
    val analysis = _analysis.asStateFlow()

    private val _resultOfEditingAnalysis =
        MutableStateFlow<ResultOfRequest<Unit>>(ResultOfRequest.Loading)
    val resultOfEditingAnalysis = _resultOfEditingAnalysis.asStateFlow()

    init {
        viewModelScope.launch {
            AnalyzesPresenter.analysis.collect {
                _analysis.value = it
                _addAnalysisScreenUiState.value = AddAnalysisScreenUiState(
                    name = analysis.value.name,
                    unit = analysis.value.unit,
                    result = analysis.value.result.toString(),
                    lowerLimit = analysis.value.lowerLimit.toString(),
                    upperLimit = analysis.value.upperLimit.toString(),
                    formattedDate = analysis.value.date,
                    nameErrorMessage = null,
                    unitErrorMessage = null,
                    resultErrorMessage = null,
                    lowerLimitErrorMessage = null,
                    upperLimitErrorMessage = null,
                )
            }
        }
    }

    fun updateName(name: String) {
        _addAnalysisScreenUiState.value = addAnalysisScreenUiState.value.copy(
            name = name,
        )
        checkName()
    }

    fun updateUnit(unit: String) {
        _addAnalysisScreenUiState.value = addAnalysisScreenUiState.value.copy(
            unit = unit,
        )
        checkUnit()
    }

    fun updateResult(result: String) {
        _addAnalysisScreenUiState.value = addAnalysisScreenUiState.value.copy(
            result = result,
        )
        checkResult()
    }

    fun updateLowerLimit(lowerLimit: String) {
        _addAnalysisScreenUiState.value = addAnalysisScreenUiState.value.copy(
            lowerLimit = lowerLimit,
        )
        checkLowerLimit()
    }

    fun updateUpperLimit(upperLimit: String) {
        _addAnalysisScreenUiState.value = addAnalysisScreenUiState.value.copy(
            upperLimit = upperLimit,
        )
        checkUpperLimit()
    }

    fun updateDate(date: LocalDate) {
        _addAnalysisScreenUiState.value = addAnalysisScreenUiState.value.copy(
            pickedDate = date,
            formattedDate = date.toFormattedDate(),
        )
    }

    private fun checkName() {
        _addAnalysisScreenUiState.value = addAnalysisScreenUiState.value.copy(
            nameErrorMessage = when {
                addAnalysisScreenUiState.value.name.isEmpty() -> R.string.empty_field
                else -> null
            }
        )
    }

    private fun checkUnit() {
        _addAnalysisScreenUiState.value = addAnalysisScreenUiState.value.copy(
            unitErrorMessage = when {
                addAnalysisScreenUiState.value.unit.isEmpty() -> R.string.empty_field
                else -> null
            }
        )
    }

    private fun checkResult() {
        _addAnalysisScreenUiState.value = addAnalysisScreenUiState.value.copy(
            resultErrorMessage = when {
                addAnalysisScreenUiState.value.result.isEmpty() -> R.string.empty_field
                else -> null
            }
        )
    }

    private fun checkLowerLimit() {
        _addAnalysisScreenUiState.value = addAnalysisScreenUiState.value.copy(
            lowerLimitErrorMessage = when {
                addAnalysisScreenUiState.value.lowerLimit.isEmpty() -> R.string.empty_field
                else -> null
            }
        )
    }

    private fun checkUpperLimit() {
        _addAnalysisScreenUiState.value = addAnalysisScreenUiState.value.copy(
            upperLimitErrorMessage = when {
                addAnalysisScreenUiState.value.upperLimit.isEmpty() -> R.string.empty_field
                else -> null
            }
        )
    }

    fun editAnalysis() {
        editingAnalysisJob?.cancel()
        val updatedAnalysis = addAnalysisScreenUiState.value.toAnalysis().copy(
            index = analysis.value.index,
        )
        editingAnalysisJob = viewModelScope.launch {
            userRepository.editAnalysis(updatedAnalysis)
            userRepository.resultOfEditingAnalysis.collect {
                _resultOfEditingAnalysis.value = it
                if (it is ResultOfRequest.Success) {
                    AnalyzesPresenter.setAnalysis(updatedAnalysis)
                }
            }
        }

    }
}