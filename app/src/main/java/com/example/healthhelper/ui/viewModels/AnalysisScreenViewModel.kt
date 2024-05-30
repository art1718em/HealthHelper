package com.example.healthhelper.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthhelper.data.repository.UserRepository
import com.example.healthhelper.domain.model.Analysis
import com.example.healthhelper.presenter.AnalyzesPresenter
import com.example.healthhelper.ui.screens.main.analysis.AnalysisUiState
import com.example.healthhelper.utils.toAnalyzesUiStateWithSort
import com.example.healthhelper.utils.toLocalDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnalysisScreenViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _analyzes = MutableStateFlow<List<Analysis>>(emptyList())
    private val analyzes = _analyzes.asStateFlow()

    private val _analyzesUiState = MutableStateFlow<List<AnalysisUiState>>(emptyList())
    val analyzesUiState = _analyzesUiState.asStateFlow()

    init {
        viewModelScope.launch {
            userRepository.analyzes.collect { analyzes ->
                _analyzes.value = analyzes.sortedByDescending { it.date.toLocalDate() }
                _analyzesUiState.value = analyzes.toAnalyzesUiStateWithSort()
            }
        }
    }

    fun addAnalysisToPresenter(index: Int) {
        AnalyzesPresenter.setAnalysis(analyzes.value[index])
    }

    fun getAnalyzesSize() = analyzes.value.lastIndex + 1
}