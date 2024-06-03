package com.example.healthhelper.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthhelper.data.repository.UserDiaryRepository
import com.example.healthhelper.domain.model.DiaryEntry
import com.example.healthhelper.presenter.DiaryProvider
import com.example.healthhelper.utils.toLocalDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiaryScreenViewModel @Inject constructor(
    private val userDiaryRepository: UserDiaryRepository,
) : ViewModel() {

    private val _diaryEntries = MutableStateFlow<List<DiaryEntry>>(emptyList())
    private val diaryEntries = _diaryEntries.asStateFlow()

    val diaryEntryUiState = diaryEntries.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        emptyList(),
    )

    init {
        viewModelScope.launch {
            userDiaryRepository.diaryEntries.collect { diaryEntries ->
                _diaryEntries.value = diaryEntries.sortedByDescending { it.date.toLocalDate() }
            }
        }
    }

    fun addDiaryEntryToPresenter(index: Int) {
        DiaryProvider.setDiaryEntry(diaryEntries.value[index])
    }

    fun getAnalyzesSize() = diaryEntries.value.size

}