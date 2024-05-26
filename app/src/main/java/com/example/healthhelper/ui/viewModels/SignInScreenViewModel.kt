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
        checkEmail(email)
    }

    fun updatePassword(password: String) {
        _signInScreenUiState.value = signInScreenUiState.value.copy(
            password = password,
        )
        checkPassword(password)
    }

    private fun checkEmail(email: String) {
        val patternEmail = Regex("""^\S+@\S+\.\S+$""")
        _signInScreenUiState.value = signInScreenUiState.value.copy(
            emailErrorMessage = when {
                email.isEmpty() -> R.string.empty_field
                !patternEmail.matches(email) -> R.string.wrong_email
                else -> null
            }
        )
    }

    private fun checkPassword(password: String) {
        _signInScreenUiState.value = signInScreenUiState.value.copy(
            passwordErrorMessage = when {
                password.isEmpty() -> R.string.empty_field
                else -> null
            }
        )
    }

    fun signIn(email: String, password: String) {
        signInJob?.cancel()
        signInJob = viewModelScope.launch {
            val result = userAuthenticationApi.signIn(email, password)
            _resultOfRequest.update { result }
        }
    }

}