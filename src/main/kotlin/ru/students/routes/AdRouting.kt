package ru.students.routes

import io.github.smiley4.ktorswaggerui.dsl.get
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.students.dtos.AdResponse
import ru.students.services.AdService

fun Application.adRouting() {
    routing {
        route("api/ads") {

            get("", {
                tags = listOf("Объявления")
                description = "Получения списка объявлений"
                response {
                    HttpStatusCode.OK to {
                        description = "Успешная получения списка объявлений"
                        body<List<AdResponse>>()
                    }
                }
            })
            {

                call.respond(AdService.getAdsResponses())
            }
        }
    }
}