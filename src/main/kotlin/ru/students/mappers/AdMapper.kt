package ru.students.mappers

import ru.students.dtos.AdResponse
import ru.students.dtos.UserResponse
import ru.students.models.ad.Ad

object AdMapper {

    fun toResponse(ad: Ad, initCandidates: Boolean = false): AdResponse {
        val tags = ad.tags.map(TagMapper::toResponse)
        val user = UserMapper.toResponse(ad.user)

        var candidates: List<UserResponse>? = null
        var executor: UserResponse? = null

        if (initCandidates) {
            candidates = ad.candidates.map(UserMapper::toResponse)
            val executorNotMapped = ad.executor
            if (executorNotMapped != null) {
                executor = UserMapper.toResponse(executorNotMapped)
            }
        }

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
            isModerated = ad.isModerated,
            candidates = candidates ?: listOf(),
            executor = executor,
            isFinished = ad.isFinished

        )
    }
}