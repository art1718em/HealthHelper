package com.example.healthhelper.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthhelper.core.ResultOfRequest
import com.example.healthhelper.data.api.UserAuthenticationApi
import com.example.healthhelper.data.repository.UserAnalyzesRepository
import com.example.healthhelper.data.repository.UserDiaryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountScreenViewModel @Inject constructor(
    private val userAuthenticationApi: UserAuthenticationApi,
    private val userAnalyzesRepository: UserAnalyzesRepository,
    private val userDiaryRepository: UserDiaryRepository,
) : ViewModel() {

    private val _userEmail = MutableStateFlow<String?>(null)
    val userEmail = _userEmail.asStateFlow()

    private var logOutJob: Job? = null

    private val _resultOfLogOut = MutableStateFlow<ResultOfRequest<Unit>>(ResultOfRequest.Loading)
    val resultOfLogOut = _resultOfLogOut.asStateFlow()

    fun getUserEmail() {
        viewModelScope.launch {
            when (val resultOfLoadingEmail = userAuthenticationApi.getUserEmail()) {
                is ResultOfRequest.Success -> _userEmail.value = resultOfLoadingEmail.result
                else -> {}
            }
        }
    }

    fun logOut() {
        logOutJob?.cancel()
        logOutJob = viewModelScope.launch {
            _resultOfLogOut.value = userAuthenticationApi.logOut()
            if (_resultOfLogOut.value is ResultOfRequest.Success) {
                userAnalyzesRepository.clearData()
                userDiaryRepository.clearData()
            }
        }
    }

}