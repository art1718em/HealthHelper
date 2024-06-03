package com.example.healthhelper.ui.screens.main.appointments

data class AppointmentUIState(
    val doctorSpecialty: String = "",
    val date: String = "",
    val time: String = "",
    val isVisited: Boolean = false,
)