package ru.students.dtos

data class AdForUserResponse(
    val ad: AdResponse,
    val candidates: List<UserResponse>,
    val executor: UserResponse?
)
