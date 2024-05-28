package com.example.healthhelper.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthhelper.R
import com.example.healthhelper.core.ResultOfRequest
import com.example.healthhelper.data.api.UserAuthenticationApi
import com.example.healthhelper.ui.screens.main.changePassword.ChangePasswordScreenUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val userAuthenticationApi: UserAuthenticationApi,
) : ViewModel() {

    private val _changePasswordScreenUiState = MutableStateFlow(ChangePasswordScreenUiState())
    val changePasswordScreenUiState = _changePasswordScreenUiState.asStateFlow()

    private val _resultOfChangingPassword =
        MutableStateFlow<ResultOfRequest<Unit>>(ResultOfRequest.Loading)
    val resultOfChangingPassword = _resultOfChangingPassword.asStateFlow()

    fun updateOldPassword(oldPassword: String) {
        _changePasswordScreenUiState.value = changePasswordScreenUiState.value.copy(
            oldPassword = oldPassword,
        )
        checkOldPassword()
    }

    fun updateNewPassword(newPassword: String) {
        _changePasswordScreenUiState.value = changePasswordScreenUiState.value.copy(
            newPassword = newPassword,
        )
        checkNewPassword()
        comparePasswords()
    }

    fun updateNewPasswordAgain(newPasswordAgain: String) {
        _changePasswordScreenUiState.value = changePasswordScreenUiState.value.copy(
            newPasswordAgain = newPasswordAgain,
        )
        comparePasswords()
    }

    private fun checkOldPassword() {
        _changePasswordScreenUiState.value = changePasswordScreenUiState.value.copy(
            oldPasswordErrorMessage = when {
                changePasswordScreenUiState.value.oldPassword.isEmpty() -> R.string.empty_field
                else -> null
            }
        )
    }

    private fun checkNewPassword() {
        _changePasswordScreenUiState.value = changePasswordScreenUiState.value.copy(
            newPasswordErrorMessage = when {
                changePasswordScreenUiState.value.newPassword.isEmpty() -> R.string.empty_field
                !Regex("""^.{8,}$""").matches(changePasswordScreenUiState.value.newPassword) -> R.string.wrong_password_length
                !Regex("""^.*[A-Z].*${'$'}""").matches(changePasswordScreenUiState.value.newPassword) -> R.string.wrong_password_uppercase_letter
                !Regex("""^.*[a-z].*${'$'}""").matches(changePasswordScreenUiState.value.newPassword) -> R.string.wrong_password_lowercase_letter
                !Regex("""^.*[0-9].*${'$'}""").matches(changePasswordScreenUiState.value.newPassword) -> R.string.wrong_password_digital
                !Regex("""^.*[!@#${'$'}%^&*()].*$""").matches(changePasswordScreenUiState.value.newPassword) -> R.string.wrong_password_special_symbol
                else -> null
            }
        )
    }

    private fun comparePasswords() {
        _changePasswordScreenUiState.value = changePasswordScreenUiState.value.copy(
            newPasswordAgainErrorMessage = if (changePasswordScreenUiState.value.newPassword != changePasswordScreenUiState.value.newPasswordAgain)
                R.string.passwords_not_equals
            else
                null
        )
    }

    fun changePassword() {
        viewModelScope.launch {
            _resultOfChangingPassword.value = userAuthenticationApi.changePassword(
                oldPassword = changePasswordScreenUiState.value.oldPassword,
                newPassword = changePasswordScreenUiState.value.newPassword,
            )
        }
    }

}