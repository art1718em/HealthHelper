package com.example.healthhelper.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthhelper.core.ResultOfRequest
import com.example.healthhelper.data.api.UserAuthenticationApi
import com.example.healthhelper.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val userAuthenticationApi: UserAuthenticationApi,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _resultOfCheckingUser =
        MutableStateFlow<ResultOfRequest<Unit>>(ResultOfRequest.Loading)
    val resultOfCheckingUser = _resultOfCheckingUser.asStateFlow()

    private val _resultOfLoadingData =
        MutableStateFlow<ResultOfRequest<Unit>>(ResultOfRequest.Loading)
    val resultOfLoadingData = _resultOfLoadingData.asStateFlow()

    fun checkUser() {
        viewModelScope.launch {
            _resultOfCheckingUser.value = userAuthenticationApi.checkUser()
        }
    }

    fun startLoadingUserData() {
        viewModelScope.launch {
            userRepository.loadUserData()
            userRepository.resultOfLoadingData.collect {
                _resultOfLoadingData.value = it
            }
        }
    }
}