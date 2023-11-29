package ru.students.services

import io.ktor.http.cio.internals.*
import ru.students.dtos.AdResponse
import ru.students.dtos.requests.CreateAdRequest
import ru.students.mappers.AdMapper
import ru.students.repos.AdRepo

object AdService {
    fun getAdsResponses(type: String?, tagIds: List<Long>): List<AdResponse> {

        return AdRepo.getAdsList()
            .filter { type == null || it.type.name == type }
            .filter { tagIds.isEmpty() || it.tags.map { tag -> tag.id }.any { id: Long -> id in tagIds } }
            .map(AdMapper::toResponse)
    }

    fun createAd(request: CreateAdRequest, userId: Long): AdResponse {
        val id = AdRepo.createAd(request, userId)
        return AdMapper.toResponse(AdRepo.findAdById(id)!!)
    }
}