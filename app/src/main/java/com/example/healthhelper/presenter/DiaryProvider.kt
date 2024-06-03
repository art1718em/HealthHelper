package com.example.healthhelper.presenter

import com.example.healthhelper.domain.model.DiaryEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object DiaryProvider {

    private val _diaryEntry = MutableStateFlow(DiaryEntry())
    val diaryEntry = _diaryEntry.asStateFlow()

    fun setDiaryEntry(diaryEntry: DiaryEntry) {
        _diaryEntry.value = diaryEntry
    }
}