package com.example.healthhelper.domain.model

import com.example.healthhelper.ui.screens.main.appointments.AppointmentUIState

data class Appointment(
    val doctorSpecialty: String = "",
    val doctorName: String = "",
    val date: String = "",
    val time: String = "",
    val address: String = "",
    val visited: Boolean = false,
    val recommendations: String? = null,
    val index: Int = 0,
) {
    fun toAppointmentUiState(): AppointmentUIState {
        return AppointmentUIState(
            doctorSpecialty = doctorSpecialty,
            date = date,
            time = time,
            isVisited = visited,
        )
    }
}