package com.example.healthhelper.utils

import java.time.LocalTime
import java.time.format.DateTimeFormatter

fun LocalTime.toFormattedTime(): String {
    return DateTimeFormatter
        .ofPattern("HH:mm")
        .format(this)
}