package com.example.healthhelper.domain.model

data class User(
    val analyzes: List<Analysis> = mutableListOf(),
)