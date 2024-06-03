package com.example.healthhelper.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthhelper.data.repository.UserAnalyzesRepository
import com.example.healthhelper.domain.model.Analysis
import com.example.healthhelper.presenter.AnalyzesProvider
import com.example.healthhelper.utils.toAnalyzesUiStateWithSort
import com.example.healthhelper.utils.toLocalDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnalysisScreenViewModel @Inject constructor(
    private val userAnalyzesRepository: UserAnalyzesRepository,
) : ViewModel() {

    private val _analyzes = MutableStateFlow<List<Analysis>>(emptyList())
    private val analyzes = _analyzes.asStateFlow()

    val analyzesUiState = analyzes.map { analyzes ->
        analyzes.toAnalyzesUiStateWithSort()
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        emptyList(),
    )

    init {
        viewModelScope.launch {
            userAnalyzesRepository.analyzes.collect { analyzes ->
                _analyzes.value = analyzes.sortedByDescending { it.date.toLocalDate() }
            }
        }
    }

    fun addAnalysisToPresenter(index: Int) {
        AnalyzesProvider.setAnalysis(analyzes.value[index])
    }

    fun getAnalyzesSize() = analyzes.value.size
}