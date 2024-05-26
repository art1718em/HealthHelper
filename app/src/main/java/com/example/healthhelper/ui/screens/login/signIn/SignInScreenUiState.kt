package com.example.healthhelper.ui.screens.login.signIn

import com.example.healthhelper.R

data class SignInScreenUiState(
    val email: String = "",
    val password: String = "",
    val emailErrorMessage: Int? = R.string.empty_field,
    val passwordErrorMessage: Int? = R.string.empty_field,
)