package com.example.healthhelper.utils

import com.example.healthhelper.domain.model.Appointment

fun List<Appointment>.sortAppointments(): List<Appointment> {
    return this
        .sortedWith(compareByDescending<Appointment> { it.date.toLocalDate() }
            .thenByDescending { it.time.toLocalTime() })
}