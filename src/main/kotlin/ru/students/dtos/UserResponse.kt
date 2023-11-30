package ru.students.dtos

data class UserResponse(val id: Long, val login: String, val rating: Int = 0)
