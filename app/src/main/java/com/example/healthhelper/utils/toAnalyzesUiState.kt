package com.example.healthhelper.utils

import com.example.healthhelper.domain.model.Analysis
import com.example.healthhelper.ui.screens.main.analysis.AnalysisUiState

fun List<Analysis>.toAnalyzesUiStateWithSort(): List<AnalysisUiState> {
    return this.sortedByDescending { it.date.toLocalDate() }.map { it.toAnalysisUiState() }
}