package ru.students.routes

import io.github.smiley4.ktorswaggerui.dsl.get
import io.github.smiley4.ktorswaggerui.dsl.post
import io.github.smiley4.ktorswaggerui.dsl.put
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.students.dtos.AdResponse
import ru.students.dtos.TagResponse
import ru.students.dtos.requests.CreateAdRequest
import ru.students.models.ad.AdType
import ru.students.services.AdService

fun Application.adRouting() {
    routing {
        route("api/ads") {

            get("", {
                tags = listOf("Объявления")
                description = "Получения списка объявлений"
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
            })
            {
                val type = call.parameters["type"]
                val title = call.parameters["title"]

                call.respond(AdService.getAdsResponses(type, title, true, null))
            }

            get("/tags",
                {
                    tags = listOf("Объявления")
                    description = "Получение списка тегов"
                    response {
                        HttpStatusCode.OK to {
                            description = "Успешное получение тегов"
                            body<List<TagResponse>>()
                        }
                    }
                }) {
                call.respond(AdService.getTags())
            }

            authenticate {

                get("/all", {
                    tags = listOf("Объявления")
                    description = "Получения списка объявлений"
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
                })
                {
                    val type = call.parameters["type"]
                    val title = call.parameters["title"]
                    val userId: Long? = call.principal<JWTPrincipal>()?.payload?.claims?.get("userId")?.asLong()

                    call.respond(AdService.getAdsResponses(type, title, true, userId))
                }

                get("/my", {
                    tags = listOf("Объявления")
                    description = "Получения списка своих объявлений"
                    response {
                        HttpStatusCode.OK to {
                            description = "Успешная получения списка объявлений"
                            body<List<AdResponse>>()
                        }
                    }
                    request {
                        pathParameter<AdType>("type") {
                            description = "SERVICE - услуга, ORDER - заказ"
                        }
                        pathParameter<Long>("title") {
                            description = "название"
                        }
                    }
                })
                {
                    val userId: Long = call.principal<JWTPrincipal>()!!.payload.claims["userId"]!!.asLong()
                    val type = call.parameters["type"]
                    val title = call.parameters["title"]
                    call.respond(AdService.getUsersAds(userId, type, title))
                }

                get("/requested", {
                    tags = listOf("Объявления")
                    description = "Получения списка объявлений, на которые откликнулся пользователь"
                    response {
                        HttpStatusCode.OK to {
                            description = "Успешная получения списка объявлений"
                            body<List<AdResponse>>()
                        }
                    }
                    request {
                        pathParameter<AdType>("type") {
                            description = "SERVICE - услуга, ORDER - заказ"
                        }
                        pathParameter<Long>("title") {
                            description = "название"
                        }
                    }
                })
                {
                    val userId: Long = call.principal<JWTPrincipal>()!!.payload.claims["userId"]!!.asLong()
                    val type = call.parameters["type"]
                    val title = call.parameters["title"]
                    call.respond(AdService.getRequestedAds(userId, type, title))
                }

                post("", {
                    tags = listOf("Объявления")
                    description = "Создание объявления"
                    request {
                        body<CreateAdRequest>()
                        body<MultiPartData>()
                    }
                }) {
                    val userId: Long = call.principal<JWTPrincipal>()!!.payload.claims["userId"]!!.asLong()
                    val multipart: MultiPartData = call.receiveMultipart()
                    val result = AdService.createAd(userId, multipart)
                    call.respond(result.code, result.data ?: result.message)
                }

                post("request/{adId}",
                    {
                        tags = listOf("Объявления")
                        description = "Отправить заявку на выполнение объявления"
                        request {
                            pathParameter<Long>("adId") {
                                description = "id объявления, на которое нужно откликнуться"
                            }
                        }
                    }
                ) {
                    val userId: Long = call.principal<JWTPrincipal>()!!.payload.claims["userId"]!!.asLong()
                    val adId = call.parameters["adId"]?.toLong()!!

                    val result = AdService.requestToAd(adId, userId)
                    call.respond(result.code, result.data ?: result.message)
                }

                put("set-executor",
                    {
                        tags = listOf("Объявления")
                        description = "Выбрать исполнителя заказа"
                        request {
                            pathParameter<Long>("adId") {
                                description = "id объявления, на которое нужно назначить исполнителя"
                            }
                            pathParameter<Long>("executorId") {
                                description = "id пользователя, которого нужно назначить исполнителем"
                            }
                        }
                    }) {
                    val userId: Long = call.principal<JWTPrincipal>()!!.payload.claims["userId"]!!.asLong()
                    val adId = call.parameters["adId"]?.toLong()!!
                    val executorId = call.parameters["executorId"]?.toLong()!!

                    val result = AdService.setExecutorToAd(userId, adId, executorId)

                    call.respond(result.code, result.data ?: result.message)
                }

                put("finish/{adId}",
                    {
                        tags = listOf("Объявления")
                        description = "Окончить исполнение заявки, перевести деньги исполнителю"
                        request {
                            pathParameter<Long>("adId") {
                                description = "id объявления, исполнение которого нужно завершить"
                            }
                        }
                    }) {
                    val userId: Long = call.principal<JWTPrincipal>()!!.payload.claims["userId"]!!.asLong()
                    val adId = call.parameters["adId"]?.toLong()!!

                    val result = AdService.finishExecution(adId, userId)

                    call.respond(result.code, result.data ?: result.message)
                }
            }
        }
    }
}