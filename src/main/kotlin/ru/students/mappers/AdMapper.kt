package ru.students.mappers

import ru.students.dtos.AdForUserResponse
import ru.students.dtos.AdResponse
import ru.students.dtos.UserResponse
import ru.students.models.ad.Ad

object AdMapper {

    fun toResponse(ad: Ad): AdResponse {
        val tags = ad.tags.map(TagMapper::toResponse)
        val user = UserMapper.toResponse(ad.user)
        val files = ad.files.map(FileMapper::toResponse)
        return AdResponse(
            id = ad.id,
            user = user,
            tags = tags,
            description = ad.description,
            title = ad.title,
            price = ad.price,
            type = ad.type,
            files = files,
            isModerated = ad.isModerated
        )
    }

    fun toUserResponse(ad: Ad): AdForUserResponse {

        val candidates = ad.candidates.map(UserMapper::toResponse)
        val executor = ad.executor

        var executorResponse: UserResponse? = null

        if (executor != null){
            executorResponse = UserMapper.toResponse(executor)
        }

        return AdForUserResponse(
            ad = toResponse(ad),
            candidates = candidates,
            executor = executorResponse
        )
    }
}