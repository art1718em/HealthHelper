package com.example.healthhelper.domain.model

data class User(
    val analyzes: List<Analysis> = mutableListOf(),
    val diaryEntries: List<DiaryEntry> = mutableListOf(),
    val appointments: List<Appointment> = mutableListOf(),
)