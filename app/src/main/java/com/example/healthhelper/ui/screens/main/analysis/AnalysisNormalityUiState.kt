package com.example.healthhelper.ui.screens.main.analysis

sealed interface AnalysisNormalityUiState {
    data object Normal : AnalysisNormalityUiState
    sealed class Deviation(
        val isUpper: Boolean,
        val diff: Double,
    ) : AnalysisNormalityUiState {
        class Medium(
            isUpper: Boolean,
            diff: Double,
        ) : Deviation(
            isUpper,
            diff,
        )

        class Bad(
            isUpper: Boolean,
            diff: Double,
        ) : Deviation(
            isUpper,
            diff,
        )
    }
}
