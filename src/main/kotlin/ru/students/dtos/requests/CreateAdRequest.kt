package ru.students.dtos.requests

import ru.students.models.ad.AdType

data class CreateAdRequest(
    val type: AdType,
    val title: String,
    val tags: List<Long>,
    val description: String,
    val price: Int
)
