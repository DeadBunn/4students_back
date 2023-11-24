package ru.students.routes

import io.github.smiley4.ktorswaggerui.dsl.get
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.students.dtos.AdResponse
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
        }
    }
}