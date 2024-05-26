package com.example.healthhelper.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthhelper.core.ResultOfRequest
import com.example.healthhelper.data.api.UserAnalyzesApi
import com.example.healthhelper.ui.screens.main.analysis.AnalysisUiState
import com.example.healthhelper.utils.toAnalyzesUiStateWithSort
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnalysisScreenViewModel @Inject constructor(
    private val userAnalyzesApi: UserAnalyzesApi
) : ViewModel() {

    private val _resultOfLoadAnalyzes =
        MutableStateFlow<ResultOfRequest<List<AnalysisUiState>>>(ResultOfRequest.Loading)
    val resultOfLoadAnalyzes = _resultOfLoadAnalyzes.asStateFlow()

    fun loadAnalyzes() {
        viewModelScope.launch {
            userAnalyzesApi.getAnalyzes { user ->
                _resultOfLoadAnalyzes.value =
                    ResultOfRequest.Success(user!!.analyzes.toAnalyzesUiStateWithSort())
            }
        }
    }
}