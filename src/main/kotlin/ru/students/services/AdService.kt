package ru.students.services

import io.ktor.http.*
import io.ktor.http.content.*
import ru.students.dtos.AdForUserResponse
import ru.students.dtos.AdResponse
import ru.students.dtos.BaseResponse
import ru.students.dtos.requests.CreateAdRequest
import ru.students.mappers.AdMapper
import ru.students.models.ad.AdType
import ru.students.models.user.User
import ru.students.repos.AdRepo
import ru.students.repos.UserRepo

object AdService {
    fun getAdsResponses(type: String?, tagIds: List<Long>, isModerated: Boolean?): List<AdResponse> {

        return AdRepo.getAdsList()
            .asSequence()
            .filter { type == null || it.type.name == type }
            .filter { tagIds.isEmpty() || it.tags.map { tag -> tag.id }.any { id: Long -> id in tagIds } }
            .filter { isModerated == null || it.isModerated == isModerated }
            .filter { it.executor == null }
            .map(AdMapper::toResponse)
            .toList()
    }

    fun getUsersAds(userId: Long, type: String?, tagIds: List<Long>): List<AdForUserResponse> {
        return AdRepo.getAdsList()
            .filter { it.user.id == userId }
            .filter { type == null || it.type.name == type }
            .filter { tagIds.isEmpty() || it.tags.map { tag -> tag.id }.any { id: Long -> id in tagIds } }
            .map(AdMapper::toUserResponse)
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

    fun approveAd(adId: Long) {
        AdRepo.setIsModeratedForAd(adId, true)
    }

    fun requestToAd(adId: Long, userId: Long): BaseResponse<String> {

        val ad = AdRepo.findAdById(adId) ?: return BaseResponse(
            code = HttpStatusCode.NotFound,
            message = "Объявление не найдено"
        )

        if (ad.type != AdType.ORDER) {
            return BaseResponse(
                code = HttpStatusCode.BadRequest,
                message = "Откликнуться можно только на заказ"
            )
        }

        if (ad.user.id == userId) {
            return BaseResponse(
                code = HttpStatusCode.MethodNotAllowed,
                message = "Нельзя откликнуться на свое объявление"
            )
        }

        if (ad.candidates.map { it.id }.contains(userId)) {
            return BaseResponse(
                code = HttpStatusCode.BadRequest,
                message = "Вы уже откликнулись на объявление"
            )
        }

        AdRepo.requestForAd(adId, userId)
        return BaseResponse(
            data = "Вы успешно откликнулись на объявление"
        )
    }

    fun setExecutorToAd(userId: Long, adId: Long, executorId: Long): BaseResponse<String> {
        val ad = AdRepo.findAdById(adId) ?: return BaseResponse(
            code = HttpStatusCode.NotFound,
            message = "Объявление не найдено"
        )

        if (ad.user.id != userId) return BaseResponse(
            code = HttpStatusCode.MethodNotAllowed,
            message = "Назначить может только автор объявления"
        )

        if (ad.type != AdType.ORDER) {
            return BaseResponse(
                code = HttpStatusCode.BadRequest,
                message = "Назначить исполнителя можно только на заказ"
            )
        }

        if (ad.executor != null) return BaseResponse(
            code = HttpStatusCode.MethodNotAllowed,
            message = "Вы уже назначили исполнителя"
        )

        if (!ad.candidates.map { it.id }.contains(executorId)) return BaseResponse(
            code = HttpStatusCode.MethodNotAllowed,
            message = "Назначить можно только откликнувшихся пользователей"
        )

        AdRepo.setExecutor(adId, executorId)

        return BaseResponse(data = "Вы успешно назначили исполнителя")
    }
}