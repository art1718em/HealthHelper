package com.example.healthhelper.presenter

import com.example.healthhelper.domain.model.Analysis
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object AnalyzesProvider {

    private val _analysis = MutableStateFlow(Analysis())
    val analysis = _analysis.asStateFlow()

    fun setAnalysis(analysis: Analysis) {
        _analysis.value = analysis
    }
}