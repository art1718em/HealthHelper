package com.example.healthhelper.ui.screens.login.signUp

import com.example.healthhelper.R

data class SignUpScreenUiState(
    val email: String = "",
    val password: String = "",
    val passwordAgain: String = "",
    val emailErrorMessage: Int? = R.string.empty_field,
    val passwordErrorMessage: Int? = R.string.empty_field,
    val passwordAgainErrorMessage: Int? = R.string.empty_field,
)