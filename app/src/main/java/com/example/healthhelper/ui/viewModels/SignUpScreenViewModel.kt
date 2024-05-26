package com.example.healthhelper.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthhelper.R
import com.example.healthhelper.core.ResultOfRequest
import com.example.healthhelper.data.api.UserAuthenticationApi
import com.example.healthhelper.domain.model.User
import com.example.healthhelper.ui.screens.login.signUp.SignUpScreenUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpScreenViewModel @Inject constructor(
    private val userAuthenticationApi: UserAuthenticationApi
) : ViewModel() {

    private val _signUpScreenUiState = MutableStateFlow(SignUpScreenUiState())
    val signUpScreenUiState = _signUpScreenUiState.asStateFlow()

    private var signUpJob: Job? = null

    private val _resultOfRequest = MutableStateFlow<ResultOfRequest<Unit>>(ResultOfRequest.Loading)
    val resultOfRequest = _resultOfRequest.asStateFlow()

    fun updateEmail(email: String) {
        _signUpScreenUiState.value = signUpScreenUiState.value.copy(
            email = email,
        )
        checkEmail(email)
    }

    fun updatePassword(password: String) {
        _signUpScreenUiState.value = signUpScreenUiState.value.copy(
            password = password,
        )
        checkPassword(password)
    }

    fun updatePasswordAgain(password: String, passwordAgain: String) {
        _signUpScreenUiState.value = signUpScreenUiState.value.copy(
            passwordAgain = passwordAgain,
        )
        comparePasswords(password, passwordAgain)
    }

    private fun checkEmail(email: String) {
        _signUpScreenUiState.value = signUpScreenUiState.value.copy(
            emailErrorMessage = when {
                email.isEmpty() -> R.string.empty_field
                !Regex("""^\S+@\S+\.\S+$""").matches(email) -> R.string.wrong_email
                else -> null
            }
        )
    }

    private fun checkPassword(password: String) {
        _signUpScreenUiState.value = signUpScreenUiState.value.copy(
            passwordErrorMessage = when {
                password.isEmpty() -> R.string.empty_field
                !Regex("""^.{8,}$""").matches(password) -> R.string.wrong_password_length
                !Regex("""^.*[A-Z].*${'$'}""").matches(password) -> R.string.wrong_password_uppercase_letter
                !Regex("""^.*[a-z].*${'$'}""").matches(password) -> R.string.wrong_password_lowercase_letter
                !Regex("""^.*[0-9].*${'$'}""").matches(password) -> R.string.wrong_password_digital
                !Regex("""^.*[!@#${'$'}%^&*()].*$""").matches(password) -> R.string.wrong_password_special_symbol
                else -> null
            }
        )
    }

    private fun comparePasswords(password: String, passwordAgain: String) {
        _signUpScreenUiState.value = signUpScreenUiState.value.copy(
            passwordAgainErrorMessage = if (password != passwordAgain)
                R.string.passwords_not_equals
            else
                null
        )
    }

    fun signUp(email: String, password: String) {
        signUpJob?.cancel()
        signUpJob = viewModelScope.launch {
            val result = userAuthenticationApi.signUp(email, password)
            if (result is ResultOfRequest.Success) {
                val user = User()
                val resultOfSetUser = userAuthenticationApi.setUser(user)
                _resultOfRequest.update { resultOfSetUser }
            } else
                _resultOfRequest.update { result }
        }
    }
}