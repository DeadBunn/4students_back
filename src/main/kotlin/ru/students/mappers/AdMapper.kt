package ru.students.mappers

import ru.students.dtos.AdResponse
import ru.students.models.ad.Ad

object AdMapper {

    fun toResponse(ad: Ad): AdResponse {
        val tags = ad.tags.map(TagMapper::toResponse)
        val user = UserMapper.toResponse(ad.user)
        return AdResponse(
            user = user,
            tags = tags,
            description = ad.description,
            title = ad.title,
            price = ad.price,
            type = ad.type
        )
    }
}