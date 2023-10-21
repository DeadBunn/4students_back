package ru.students.requests

data class RegisterUser(
    val login: String,
    val email: String,
    val password: String
)