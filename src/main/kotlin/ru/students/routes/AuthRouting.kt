package ru.students.routes

import io.github.smiley4.ktorswaggerui.dsl.post
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.students.dtos.JWTResponse
import ru.students.requests.UserCredentials
import ru.students.services.AuthService

fun Application.authRouting() {
    routing {
        route("api/auth") {

            post("/login", {
                tags = listOf("Авторизация")
                description = "Авторизация через логин и пароль"
                request {
                    body<UserCredentials>()
                }
                response {
                    HttpStatusCode.OK to {
                        description = "Успешная авторизация"
                        body<JWTResponse>()
                    }
                    HttpStatusCode.Unauthorized to {
                        description = "Неправильный логин или пароль"
                    }
                }
            })
            {
                val credits = call.receive<UserCredentials>()
                val result = AuthService.authenticate(credits)
                call.respond(result.code, result.data ?: result.message)
            }

            post("/refresh", {
                tags = listOf("Авторизация")
                description = "Авторизация через refresh-токен"
                request {
                    pathParameter<String>("token")
                }
                response {
                    HttpStatusCode.OK to {
                        body<JWTResponse>()
                    }
                    HttpStatusCode.BadRequest to {
                        description = "Ошибка авторизации через refresh-token"
                    }
                }
            })
            {
                val token = call.request.queryParameters["token"]
                val result = AuthService.authenticateRefreshToken(token)
                call.respond(result.code, result.data ?: result.message)
            }
        }
    }
}