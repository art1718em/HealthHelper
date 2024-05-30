package com.example.healthhelper.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthhelper.domain.model.Analysis
import com.example.healthhelper.presenter.AnalyzesPresenter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnalysisDetailsScreenViewModel @Inject constructor(

) : ViewModel() {

    private val _analysis = MutableStateFlow<Analysis>(Analysis())
    val analysis = _analysis.asStateFlow()

    init {
        viewModelScope.launch {
            AnalyzesPresenter.analysis.collect {
                _analysis.value = it
            }
        }
    }

}