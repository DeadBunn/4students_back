package ru.students.routes

import io.github.smiley4.ktorswaggerui.dsl.get
import io.github.smiley4.ktorswaggerui.dsl.post
import io.ktor.http.*
import io.ktor.http.cio.internals.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.students.dtos.AdResponse
import ru.students.dtos.requests.CreateAdRequest
import ru.students.models.ad.AdType
import ru.students.services.AdService

fun Application.adRouting() {
    routing {
        route("api/ads") {

            authenticate {
                get("", {
                    tags = listOf("Объявления")
                    description = "Получения списка объявлений"
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
                })
                {
                    val type = call.parameters["type"]
                    val tagIds = call.parameters.getAll("tag")

                    val tags: List<Long> = tagIds?.map { it.toLong() } ?: listOf()
                    call.respond(AdService.getAdsResponses(type, tags))
                }

                post("", {
                    tags = listOf("Объявления")
                    description = "Создание объявления"
                    request {
                        body<CreateAdRequest>()
                    }
                }) {
                    val request = call.receive<CreateAdRequest>()
                    val principal = call.principal<JWTPrincipal>()
                    println(principal)
                    val userId: Long = call.principal<JWTPrincipal>()!!.payload.claims["userId"]!!.asLong()
                    call.respond(AdService.createAd(request, userId))
                }
            }
        }
    }
}