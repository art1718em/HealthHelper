package com.example.healthhelper.ui.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthhelper.R
import com.example.healthhelper.core.ResultOfRequest
import com.example.healthhelper.data.api.UserApi
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpScreenViewModel @Inject constructor(
    private val userApi: UserApi
) : ViewModel() {

    private val _email = MutableStateFlow("");
    val email = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _passwordAgain = MutableStateFlow("")
    val passwordAgain = _passwordAgain.asStateFlow()

    private val _isEmailCorrect = MutableStateFlow(false)
    val isEmailCorrect = _isEmailCorrect.asStateFlow()

    private val _isPasswordCorrect = MutableStateFlow(false)
    val isPasswordCorrect = _isPasswordCorrect.asStateFlow()

    private val _isPasswordAgainCorrect = MutableStateFlow(false)
    val isPasswordAgainCorrect = _isPasswordAgainCorrect.asStateFlow()

    private val _emailErrorMessage = MutableStateFlow("Поле не должно пустым")
    val emailErrorMessage = _emailErrorMessage.asStateFlow()

    private val _passwordErrorMessage = MutableStateFlow("Поле не должно пустым")
    val passwordErrorMessage = _passwordErrorMessage.asStateFlow()

    private val _passwordAgainErrorMessage = MutableStateFlow("Поле не должно пустым")
    val passwordAgainErrorMessage = _passwordAgainErrorMessage.asStateFlow()

    private val _resultOfRequest = MutableStateFlow<ResultOfRequest<FirebaseUser>?>(null)
    val resultOfRequest = _resultOfRequest.asStateFlow()

    fun updateEmail(email: String, context: Context){
        _email.value = email
        _isEmailCorrect.value = checkEmail(email, context)
    }

    fun updatePassword(password: String, context: Context){
        _password.value = password
        _isPasswordCorrect.value = checkPassword(password, context)
    }

    fun updatePasswordAgain(password: String, passwordAgain: String, context: Context){
        _passwordAgain.value = passwordAgain
        _isPasswordAgainCorrect.value = comparePasswords(password, passwordAgain, context)
    }

    private fun checkEmail(email: String, context: Context) : Boolean{
        val patternEmail = Regex("""^\S+@\S+\.\S+$""")
        _emailErrorMessage.value = when{
            email.isEmpty() -> context.getString(R.string.empty_field)
            !patternEmail.matches(email) -> context.getString(R.string.wrong_email)
            else -> ""
        }
        return _emailErrorMessage.value == ""
    }

    private fun checkPassword(password: String, context: Context) : Boolean{
        _passwordErrorMessage.value = when{
            password.isEmpty() ->
                context.getString(R.string.empty_field)
            !Regex("""^.{8,}$""").matches(password) ->
                context.getString(R.string.wrong_password_length)
            !Regex("""^.*[A-Z].*${'$'}""").matches(password) ->
                context.getString(R.string.wrong_password_uppercase_letter)
            !Regex("""^.*[a-z].*${'$'}""").matches(password) ->
                context.getString(R.string.wrong_password_lowercase_letter)
            !Regex("""^.*[0-9].*${'$'}""").matches(password) ->
                context.getString(R.string.wrong_password_digital)
            !Regex("""^.*[!@#${'$'}%^&*()].*$""").matches(password) ->
                context.getString(R.string.wrong_password_special_symbol)
            else -> ""
        }
        return _passwordErrorMessage.value == ""
    }

    private fun comparePasswords(password: String, passwordAgain: String, context: Context) : Boolean{
        _passwordAgainErrorMessage.value = if (password != passwordAgain)
             context.getString(R.string.passwords_not_equals)
        else
            ""
        return _passwordAgainErrorMessage.value == ""
    }

    fun signUp(email: String, password: String){
        viewModelScope.launch {
            val result = userApi.signUp(email, password)
            _resultOfRequest.update { result }
        }
    }
}