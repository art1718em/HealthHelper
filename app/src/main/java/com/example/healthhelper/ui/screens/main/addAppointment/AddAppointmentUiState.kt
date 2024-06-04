package com.example.healthhelper.ui.screens.main.addAppointment

import com.example.healthhelper.R
import com.example.healthhelper.domain.model.Appointment
import com.example.healthhelper.utils.toFormattedDate
import com.example.healthhelper.utils.toFormattedTime
import java.time.LocalDate
import java.time.LocalTime

data class AddAppointmentUiState(
    val doctorSpecialty: String = "",
    val doctorName: String = "",
    val formattedDate: String = LocalDate.now().toFormattedDate(),
    val pickedDate: LocalDate = LocalDate.now(),
    val pickedTime: LocalTime = LocalTime.now(),
    val formattedTime: String = LocalTime.now().toFormattedTime(),
    val address: String = "",
    val visited: Boolean = false,
    val recommendations: String? = null,
    val doctorSpecialtyErrorMessage: Int? = R.string.empty_field,
    val doctorNameErrorMessage: Int? = R.string.empty_field,
    val addressErrorMessage: Int? = R.string.empty_field,
    val recommendationsErrorMessage: Int? = R.string.empty_field,
) {
    fun toAppointment(): Appointment {
        return Appointment(
            doctorSpecialty = doctorSpecialty,
            doctorName = doctorName,
            date = formattedDate,
            time = formattedTime,
            address = address,
            recommendations = recommendations,
        )
    }
}