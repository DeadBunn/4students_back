package ru.students.routes

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
                        pathParameter<Long>("tag") {
                            description = "тег (можно добавлять несколько)"
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
                    val tagIds = call.parameters.getAll("tag")

                    val tags: List<Long> = tagIds?.map { it.toLong() } ?: listOf()
                    call.respond(AdService.getAdsResponses(type, tags, false))
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
            }
        }
    }
}