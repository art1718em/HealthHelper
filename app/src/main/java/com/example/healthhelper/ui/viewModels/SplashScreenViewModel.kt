package com.example.healthhelper.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthhelper.core.ResultOfRequest
import com.example.healthhelper.data.api.UserAuthenticationApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val userAuthenticationApi: UserAuthenticationApi,
) : ViewModel() {

    private val _result = MutableStateFlow<ResultOfRequest<Unit>>(ResultOfRequest.Loading)
    val result = _result.asStateFlow()

    fun checkUser() {
        viewModelScope.launch {
            _result.value = userAuthenticationApi.checkUser()
        }
    }
}