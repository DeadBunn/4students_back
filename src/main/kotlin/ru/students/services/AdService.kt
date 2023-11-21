package ru.students.services

import ru.students.dtos.AdResponse
import ru.students.mappers.AdMapper
import ru.students.repos.AdRepo

object AdService {
    fun getAdsResponses(): List<AdResponse> {
        return AdRepo.getAdsList().map(AdMapper::toResponse)
    }
}