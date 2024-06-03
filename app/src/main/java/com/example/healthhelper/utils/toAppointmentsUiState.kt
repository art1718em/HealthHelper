package com.example.healthhelper.utils

import com.example.healthhelper.domain.model.Appointment
import com.example.healthhelper.ui.screens.main.appointments.AppointmentUIState

fun List<Appointment>.toAppointmentUiState(): List<AppointmentUIState> {

    val visitedAppointments = filter { it.isVisited }.map { it.toAppointmentUiState() }

    val notVisitedAppointments = filter { !it.isVisited }.map { it.toAppointmentUiState() }

    return notVisitedAppointments + visitedAppointments
}