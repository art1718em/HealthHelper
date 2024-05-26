package com.example.healthhelper.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun LocalDate.toFormattedDate(): String {
    return DateTimeFormatter
        .ofPattern("dd MMM yyyy")
        .format(this)
}