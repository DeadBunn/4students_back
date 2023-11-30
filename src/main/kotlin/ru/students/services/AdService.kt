package ru.students.services

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

    fun createAd(request: CreateAdRequest, userId: Long, files: MutableList<Long>): AdResponse {
        val id = AdRepo.createAd(request, userId)

        AdRepo.linkTagsToAd(id, request.tags)
        AdRepo.linkFilesToAd(id, files)

        return AdMapper.toResponse(AdRepo.findAdById(id)!!)
    }
}