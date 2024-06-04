package com.example.healthhelper.presenter

import com.example.healthhelper.domain.model.Appointment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object AppointmentsProvider {

    private val _appointment = MutableStateFlow(Appointment())
    val appointment = _appointment.asStateFlow()

    fun setAppointment(appointment: Appointment) {
        _appointment.value = appointment
    }
}