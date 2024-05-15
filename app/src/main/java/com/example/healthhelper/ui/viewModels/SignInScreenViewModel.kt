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
class SignInScreenViewModel @Inject constructor(
    private val userApi: UserApi
) : ViewModel() {

    private val _email = MutableStateFlow("");
    val email = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _isEmailCorrect = MutableStateFlow(false)
    val isEmailCorrect = _isEmailCorrect.asStateFlow()

    private val _isPasswordCorrect = MutableStateFlow(false)
    val isPasswordCorrect = _isPasswordCorrect.asStateFlow()

    private val _emailErrorMessage = MutableStateFlow("Поле не должно пустым")
    val emailErrorMessage = _emailErrorMessage.asStateFlow()

    private val _passwordErrorMessage = MutableStateFlow("Поле не должно пустым")
    val passwordErrorMessage = _passwordErrorMessage.asStateFlow()

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
            else -> ""
        }
        return _passwordErrorMessage.value == ""
    }

    fun signIn(email: String, password: String){
        viewModelScope.launch {
            val result = userApi.signIn(email, password)
            _resultOfRequest.update { result }
        }
    }

}