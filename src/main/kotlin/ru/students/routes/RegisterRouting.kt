package ru.students.routes

import io.github.smiley4.ktorswaggerui.dsl.post
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.students.dtos.JWTResponse
import ru.students.requests.RegisterUser
import ru.students.services.RegisterService

fun Application.registerRouting() {
    routing {
        route("api/register") {
            post({
                tags = listOf("Регистрация и авторизация")
                description = "Регистрация"
                request {
                    body<RegisterUser>()
                }
                response {
                    HttpStatusCode.OK to {
                        description = "Успешная регистрация"
                        body<JWTResponse>()
                    }
                    HttpStatusCode.BadRequest to {
                        description = "Пользователь с данной почтой уже зарегистрирован<br>" +
                                "Пользователь с данным логином уже зарегистрирован"
                    }
                    HttpStatusCode.InternalServerError to {
                        description = "Ошибка создания пользователя"
                    }
                }
            })
            {
                val info = call.receive<RegisterUser>()
                val result = RegisterService.registerUser(info)
                call.respond(result.code, result.data ?: result.message)
            }
        }
    }
}