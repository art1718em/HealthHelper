package com.example.healthhelper.utils

import com.example.healthhelper.domain.model.Appointment
import com.example.healthhelper.ui.screens.main.appointments.AppointmentUIState

fun List<Appointment>.toAppointmentUiState(): List<AppointmentUIState> {
    return this.map { it.toAppointmentUiState() }
}