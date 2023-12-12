package ru.students.dtos

import ru.students.models.user.Role

data class JWTResponse(
    val tokenType: String = "Bearer",
    val accessToken: String,
    val refreshToken: String,
    val id: Long,
    val role: Role,
    val email: String,
    val login: String
)