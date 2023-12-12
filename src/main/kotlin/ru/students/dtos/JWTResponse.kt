package ru.students.dtos

data class JWTResponse(
    val tokenType: String = "Bearer",
    val accessToken: String,
    val refreshToken: String,
    val id : Long
)