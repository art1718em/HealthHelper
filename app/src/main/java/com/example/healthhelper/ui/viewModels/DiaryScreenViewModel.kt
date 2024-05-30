package com.example.healthhelper.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthhelper.data.repository.UserRepository
import com.example.healthhelper.domain.model.DiaryEntry
import com.example.healthhelper.utils.toLocalDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiaryScreenViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _diaryEntries = MutableStateFlow<List<DiaryEntry>>(emptyList())
    val diaryEntries = _diaryEntries.asStateFlow()

    init {
        viewModelScope.launch {
            userRepository.diaryEntries.collect { diaryEntries ->
                _diaryEntries.value = diaryEntries.sortedByDescending { it.date.toLocalDate() }
            }
        }
    }

}