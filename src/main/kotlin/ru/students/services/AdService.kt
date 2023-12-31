package ru.students.services

import io.ktor.http.*
import io.ktor.http.content.*
import ru.students.dtos.AdResponse
import ru.students.dtos.BaseResponse
import ru.students.dtos.TagResponse
import ru.students.dtos.requests.CreateAdRequest
import ru.students.mappers.AdMapper
import ru.students.mappers.TagMapper
import ru.students.models.ad.AdType
import ru.students.models.user.User
import ru.students.repos.AdRepo
import ru.students.repos.UserRepo

object AdService {
    fun getAdsResponses(type: String?, title: String?, isModerated: Boolean?, userId: Long? = null): List<AdResponse> {

        return AdRepo.getAdsList()
            .asSequence()
            .filter { type == null || it.type.name == type }
            .filter { title == null || it.title.lowercase().contains(title.lowercase()) }
            .filter { isModerated == null || it.isModerated == isModerated }
            .filter { it.executor == null }
            .filter { userId == null || !it.candidates.map { candidate -> candidate.id }.contains(userId) }
            .filter { !it.isFinished }
            .map(AdMapper::toResponse)
            .toList()
    }

    fun getTags(): List<TagResponse> {
        return AdRepo.getTagsList()
            .map(TagMapper::toResponse)
    }

    fun getUsersAds(userId: Long, type: String?, title: String?): List<AdResponse> {
        return AdRepo.getAdsList()
            .filter { it.user.id == userId }
            .filter { type == null || it.type.name == type }
            .filter { title == null || it.title.lowercase().contains(title.lowercase()) }
            .map { (AdMapper.toResponse(it, true)) }
    }

    fun getRequestedAds(userId: Long, type: String?, title: String?): List<AdResponse> {
        return AdRepo.getAdsList()
            .filter { it.candidates.map { user -> user.id }.contains(userId) }
            .filter { type == null || it.type.name == type }
            .filter { title == null || it.title.lowercase().contains(title.lowercase()) }
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
            return BaseResponse(
                code = HttpStatusCode.MethodNotAllowed,
                message = "На балансе недостаточно средств"
            )
        }

        if (request.price < 0) {
            return BaseResponse(
                code = HttpStatusCode.MethodNotAllowed,
                message = "Цена не может быть меньше нуля"
            )
        }

        if (request.title.isEmpty()) {
            return BaseResponse(
                code = HttpStatusCode.MethodNotAllowed,
                message = "Название не может быть пустым"
            )
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

    fun deleteAd(adId: Long): BaseResponse<String> {
        val ad = AdRepo.findAdById(adId) ?: return BaseResponse(
            code = HttpStatusCode.NotFound,
            message = "Объявление не найдено"
        )

        if (ad.isModerated) return BaseResponse(
            code = HttpStatusCode.MethodNotAllowed,
            message = "Нельзя удалить проверенное объявление"
        )

        AdRepo.deleteAd(ad.id, ad.user.id, ad.price)
        return BaseResponse(data = "Объявление успешно отклонено")
    }

    fun requestToAd(adId: Long, userId: Long): BaseResponse<String> {

        val ad = AdRepo.findAdById(adId) ?: return BaseResponse(
            code = HttpStatusCode.NotFound,
            message = "Объявление не найдено"
        )


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

        if (ad.type == AdType.ORDER) {
            AdRepo.requestForAdOrder(adId, userId)
            return BaseResponse(
                data = "Вы успешно откликнулись на объявление"
            )
        } else {
            val user = UserRepo.findUserById(userId)!!
            if (user.balance < ad.price) {
                return BaseResponse(
                    data = "На вашем счету недостаточно средств"
                )
            }
            AdRepo.requestForAdService(adId, userId, ad.price)
            return BaseResponse(
                data = "Вы успешно откликнулись на объявление"
            )
        }
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

    fun finishExecution(adId: Long, userId: Long): BaseResponse<String> {

        val ad = AdRepo.findAdById(adId) ?: return BaseResponse(
            code = HttpStatusCode.NotFound,
            message = "Объявление не найдено"
        )

        if (ad.type == AdType.ORDER) {

            if (ad.user.id != userId) return BaseResponse(
                code = HttpStatusCode.MethodNotAllowed,
                message = "Завершить исполнение может только автор объявления"
            )

            if (ad.executor == null) return BaseResponse(
                code = HttpStatusCode.NotFound,
                message = "Исполнитель не назначен"
            )

            if (ad.isFinished) return BaseResponse(
                code = HttpStatusCode.MethodNotAllowed,
                message = "Объявление уже завершено"
            )

            AdRepo.finishExecution(adId, ad.executor!!.id, ad.price)

            return BaseResponse(data = "Исполнение объявления успешно завершено")
        } else {

            if (!ad.candidates.map { it.id }.contains(userId)) {
                return BaseResponse(
                    code = HttpStatusCode.MethodNotAllowed,
                    message = "Вы не отликались на это объявление"
                )
            }

            AdRepo.finishService(adId, userId, ad.user.id, ad.price)

            return BaseResponse(data = "Заказ объявления успешно завершено")
        }
    }
}