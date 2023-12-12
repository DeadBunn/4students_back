package ru.students.routes

import io.github.smiley4.ktorswaggerui.dsl.delete
import io.github.smiley4.ktorswaggerui.dsl.get
import io.github.smiley4.ktorswaggerui.dsl.put
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.students.dtos.AdResponse
import ru.students.models.ad.AdType
import ru.students.services.AdService

fun Application.moderatorRouting() {
    routing {
        route("api/moderators") {
            authenticate("moderator") {
                get("/ads", {
                    tags = listOf("Модерирование")
                    description = "Получение списка непроверенных объявлений"
                    request {
                        pathParameter<AdType>("type") {
                            description = "SERVICE - услуга, ORDER - заказ"
                        }
                        pathParameter<Long>("title") {
                            description = "название"
                        }
                    }
                    response {
                        HttpStatusCode.OK to {
                            description = "Успешная получения списка объявлений"
                            body<List<AdResponse>>()
                        }
                    }
                }) {
                    val type = call.parameters["type"]
                    val title = call.parameters["title"]
                    call.respond(AdService.getAdsResponses(type, title, false))
                }

                put("/approve/{adId}", {
                    tags = listOf("Модерирование")
                    description = "Одобрить объявление"
                    request {
                        pathParameter<Long>("adId") {
                            description = "ID объявления, которое нужно апрувнуть"
                        }
                    }
                }) {
                    val adId = call.parameters["adId"] ?: call.respond("Объявление не найден")
                    AdService.approveAd(adId.toString().toLong())
                    call.respond("Объявление успешно одобрено")
                }

                delete("/delete/{adId}", {
                    tags = listOf("Модерирование")
                    description = "Отклонить объявление"
                    request {
                        pathParameter<Long>("adId") {
                            description = "ID объявления, которое нужно удалить"
                        }
                    }
                }) {
                    val adId = call.parameters["adId"] ?: call.respond("Объявление не найден")

                    val result = AdService.deleteAd(adId.toString().toLong())
                    call.respond(result.code, result.data ?: result.message)
                }
            }
        }
    }
}