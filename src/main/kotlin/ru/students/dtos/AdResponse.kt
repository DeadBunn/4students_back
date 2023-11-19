package ru.students.dtos

data class AdResponse(
    val user: UserResponse,
    val tags: List<TagResponse>,
    val price: Int,
    val name: String,
    val description: String,
)
