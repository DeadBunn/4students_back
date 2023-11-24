package ru.students.services

import ru.students.dtos.AdResponse
import ru.students.mappers.AdMapper
import ru.students.repos.AdRepo

object AdService {
    fun getAdsResponses(type: String?, tagIds: List<Long>): List<AdResponse> {

        return AdRepo.getAdsList()
            .filter { type == null || it.type.name == type }
            .filter { tagIds.isEmpty() || it.tags.map { tag -> tag.id }.any { id: Long -> id in tagIds } }
            .map(AdMapper::toResponse)
    }
}