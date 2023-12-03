package ru.students.dtos

import ru.students.models.ad.AdType

data class AdResponse(
    val id: Long,
    val user: UserResponse,
    val tags: List<TagResponse>,
    val price: Int,
    val title: String,
    val description: String,
    val type: AdType,
    val files: List<FileResponse>,
    val isModerated: Boolean
)
