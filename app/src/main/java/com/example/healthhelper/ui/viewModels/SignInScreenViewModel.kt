package com.example.healthhelper.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthhelper.R
import com.example.healthhelper.core.ResultOfRequest
import com.example.healthhelper.data.api.UserAuthenticationApi
import com.example.healthhelper.ui.screens.login.signIn.SignInScreenUiState
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInScreenViewModel @Inject constructor(
    private val userAuthenticationApi: UserAuthenticationApi
) : ViewModel() {

    private val _signInScreenUiState = MutableStateFlow(SignInScreenUiState())
    val signInScreenUiState = _signInScreenUiState.asStateFlow()

    private var signInJob: Job? = null

    private val _resultOfRequest =
        MutableStateFlow<ResultOfRequest<FirebaseUser>>(ResultOfRequest.Loading)
    val resultOfRequest = _resultOfRequest.asStateFlow()

    fun updateEmail(email: String) {
        _signInScreenUiState.value = signInScreenUiState.value.copy(
            email = email,
        )
        checkEmail()
    }

    fun updatePassword(password: String) {
        _signInScreenUiState.value = signInScreenUiState.value.copy(
            password = password,
        )
        checkPassword()
    }

    private fun checkEmail() {
        val patternEmail = Regex("""^\S+@\S+\.\S+$""")
        _signInScreenUiState.value = signInScreenUiState.value.copy(
            emailErrorMessage = when {
                signInScreenUiState.value.email.isEmpty() -> R.string.empty_field
                !patternEmail.matches(signInScreenUiState.value.email) -> R.string.wrong_email
                else -> null
            }
        )
    }

    private fun checkPassword() {
        _signInScreenUiState.value = signInScreenUiState.value.copy(
            passwordErrorMessage = when {
                signInScreenUiState.value.password.isEmpty() -> R.string.empty_field
                else -> null
            }
        )
    }

    fun signIn() {
        signInJob?.cancel()
        signInJob = viewModelScope.launch {
            val result = userAuthenticationApi.signIn(
                email = signInScreenUiState.value.email,
                password = signInScreenUiState.value.password,
            )
            _resultOfRequest.update { result }
        }
    }

}