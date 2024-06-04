package com.example.healthhelper.utils

import com.example.healthhelper.domain.model.Appointment

fun sort(appointments: List<Appointment>): List<Appointment> {

    return appointments
        .sortedWith(compareByDescending<Appointment> { it.date.toLocalDate() }
            .thenByDescending { it.time.toLocalTime() })
}

fun List<Appointment>.sortAppointments(): List<Appointment> {

    val visitedAppointments = sort(filter { it.visited })

    val notVisitedAppointments = sort(filter { !it.visited })

    return notVisitedAppointments + visitedAppointments
}