package ru.students.mappers

import ru.students.dtos.TagResponse
import ru.students.models.ad.Tag

object TagMapper {

    fun toResponse(tag: Tag): TagResponse {
        return TagResponse(tag.id, tag.name, tag.useCount)
    }
}