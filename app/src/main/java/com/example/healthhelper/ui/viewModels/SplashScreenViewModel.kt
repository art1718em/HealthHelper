package com.example.healthhelper.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthhelper.core.ResultOfRequest
import com.example.healthhelper.data.api.UserAuthenticationApi
import com.example.healthhelper.data.repository.UserAnalyzesRepository
import com.example.healthhelper.data.repository.UserDiaryRepository
import com.example.healthhelper.utils.LOADING_DATA_ERROR
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val userAuthenticationApi: UserAuthenticationApi,
    private val userAnalyzesRepository: UserAnalyzesRepository,
    private val userDiaryRepository: UserDiaryRepository,
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
            userAnalyzesRepository.loadUserAnalyzes()
            userDiaryRepository.loadUserDiaryEntries()

            combine(
                userAnalyzesRepository.resultOfLoadingAnalyzes,
                userDiaryRepository.resultOfLoadingDiaryEntries
            ) { analyzesResult, diaryResult ->
                when {
                    analyzesResult is ResultOfRequest.Error -> ResultOfRequest.Error(analyzesResult.errorMessage)
                    diaryResult is ResultOfRequest.Error -> ResultOfRequest.Error(diaryResult.errorMessage)
                    analyzesResult is ResultOfRequest.Success && diaryResult is ResultOfRequest.Success -> ResultOfRequest.Success(
                        Unit
                    )

                    else -> ResultOfRequest.Error(LOADING_DATA_ERROR)
                }
            }.collect { combinedResult ->
                _resultOfLoadingData.value = combinedResult
            }
        }
    }
}