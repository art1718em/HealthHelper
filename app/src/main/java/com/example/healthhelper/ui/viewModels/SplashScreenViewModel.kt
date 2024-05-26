package com.example.healthhelper.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthhelper.core.ResultOfRequest
import com.example.healthhelper.utils.NO_SIGN_ERROR_MESSAGE
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _result = MutableStateFlow<ResultOfRequest<Unit>>(ResultOfRequest.Loading)
    val result = _result.asStateFlow()

    fun checkUser() {
        viewModelScope.launch {
            if (auth.currentUser != null)
                _result.update { ResultOfRequest.Success(Unit) }
            else
                _result.update { ResultOfRequest.Error(NO_SIGN_ERROR_MESSAGE) }
        }
    }
}