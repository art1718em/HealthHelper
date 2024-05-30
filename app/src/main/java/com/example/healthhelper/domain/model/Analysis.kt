package com.example.healthhelper.domain.model

import com.example.healthhelper.ui.screens.main.analysis.AnalysisNormalityUiState
import com.example.healthhelper.ui.screens.main.analysis.AnalysisUiState

const val LOWER_LIMIT_COEFFICIENT = 0.7
const val UPPER_LIMIT_COEFFICIENT = 1.3

data class Analysis(
    val name: String = "",
    val unit: String = "",
    val result: Double = 0.0,
    val lowerLimit: Double = 0.0,
    val upperLimit: Double = 0.0,
    val date: String = "",
    val index: Int = 0,
) {
    fun toAnalysisUiState(): AnalysisUiState {
        val normality = if (result in lowerLimit..upperLimit) {
            AnalysisNormalityUiState.Normal
        } else if (lowerLimit * LOWER_LIMIT_COEFFICIENT <= result && result < lowerLimit) {
            AnalysisNormalityUiState.Deviation.Medium(
                isUpper = false,
                diff = lowerLimit - result,
            )
        } else if (result > upperLimit && result <= upperLimit * UPPER_LIMIT_COEFFICIENT) {
            AnalysisNormalityUiState.Deviation.Medium(
                isUpper = true,
                diff = result - upperLimit,
            )
        } else if (lowerLimit * LOWER_LIMIT_COEFFICIENT > result) {
            AnalysisNormalityUiState.Deviation.Bad(
                isUpper = false,
                diff = lowerLimit - result,
            )
        } else {
            AnalysisNormalityUiState.Deviation.Bad(
                isUpper = true,
                diff = result - upperLimit,
            )
        }

        return AnalysisUiState(
            name,
            unit,
            result,
            normality,
        )
    }
}

