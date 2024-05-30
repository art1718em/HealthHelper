package com.example.healthhelper.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthhelper.R
import com.example.healthhelper.core.ResultOfRequest
import com.example.healthhelper.data.repository.UserRepository
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
    private val userRepository: UserRepository,
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

    fun addAnalysis() {
        addingAnalysisJob?.cancel()
        val analysis = addAnalysisScreenUiState.value.toAnalysis()
        addingAnalysisJob = viewModelScope.launch {
            userRepository.addAnalysis(analysis)
            userRepository.resultOfAddingAnalysis.collect {
                _resultOfAddingAnalysis.value = it
            }
        }
    }
}


