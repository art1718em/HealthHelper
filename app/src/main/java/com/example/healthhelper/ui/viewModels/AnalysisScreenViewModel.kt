package com.example.healthhelper.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthhelper.data.repository.UserRepository
import com.example.healthhelper.ui.screens.main.analysis.AnalysisUiState
import com.example.healthhelper.utils.toAnalyzesUiStateWithSort
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnalysisScreenViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _analyzes = MutableStateFlow<List<AnalysisUiState>>(emptyList())
    val analyzes = _analyzes.asStateFlow()

    init {
        viewModelScope.launch {
            userRepository.analyzes.collect { analyzes ->
                _analyzes.value = analyzes.toAnalyzesUiStateWithSort()
            }
        }
    }
}