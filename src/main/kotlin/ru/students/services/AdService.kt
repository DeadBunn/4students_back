package ru.students.services

import io.ktor.http.*
import io.ktor.http.content.*
import ru.students.dtos.AdResponse
import ru.students.dtos.BaseResponse
import ru.students.dtos.requests.CreateAdRequest
import ru.students.mappers.AdMapper
import ru.students.models.ad.AdType
import ru.students.models.user.User
import ru.students.repos.AdRepo
import ru.students.repos.UserRepo

object AdService {
    fun getAdsResponses(type: String?, tagIds: List<Long>): List<AdResponse> {

        return AdRepo.getAdsList()
            .filter { type == null || it.type.name == type }
            .filter { tagIds.isEmpty() || it.tags.map { tag -> tag.id }.any { id: Long -> id in tagIds } }
            .map(AdMapper::toResponse)
    }

    suspend fun createAd(userId: Long, multipart: MultiPartData): BaseResponse<AdResponse> {
        val fileParts = mutableListOf<PartData.FileItem>()
        var jsonValue: String? = null
        multipart.forEachPart { part ->
            when (part) {
                is PartData.FileItem -> {
                    fileParts.add(part)
                }

                is PartData.FormItem -> {
                    jsonValue = part.value
                    part.dispose()
                }

                else -> {
                }
            }
        }
        val request = GsonParser.parse(CreateAdRequest::class.java, jsonValue!!)

        val user: User = UserRepo.findUserById(userId)!!

        if (request.type == AdType.ORDER && user.balance < request.price) {
            return BaseResponse(code = HttpStatusCode.BadRequest, message = "На балансе недостаточно средств")
        }

        val fileIds = mutableListOf<Long>()
        fileParts.forEach { filePart ->
            val fileId = FileService.saveFile(filePart)
            fileIds.add(fileId)
            filePart.dispose()
        }

        return BaseResponse(
            data = AdMapper.toResponse(
                AdRepo.createAd(
                    request = request,
                    userId = userId,
                    fileIds = fileIds
                )
            )
        )
    }
}