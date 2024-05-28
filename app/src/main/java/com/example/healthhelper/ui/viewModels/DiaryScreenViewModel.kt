package com.example.healthhelper.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthhelper.core.ResultOfRequest
import com.example.healthhelper.data.api.UserApi
import com.example.healthhelper.domain.model.DiaryEntry
import com.example.healthhelper.utils.USER_UNAUTHORIZED_ERROR_MESSAGE
import com.example.healthhelper.utils.toLocalDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiaryScreenViewModel @Inject constructor(
    private val userApi: UserApi,
) : ViewModel() {

    private val _resultOfLoadingDiaryEntries =
        MutableStateFlow<ResultOfRequest<List<DiaryEntry>>>(ResultOfRequest.Loading)
    val resultOfLoadingDiaryEntries = _resultOfLoadingDiaryEntries.asStateFlow()

    fun loadDiaryEntries() {
        viewModelScope.launch {
            userApi.getUserData { currentUser ->
                _resultOfLoadingDiaryEntries.value = currentUser?.let { user ->
                    ResultOfRequest.Success(user.diaryEntries.sortedByDescending {
                        it.date.toLocalDate()
                    })
                } ?: ResultOfRequest.Error(USER_UNAUTHORIZED_ERROR_MESSAGE)
            }
        }
    }

}