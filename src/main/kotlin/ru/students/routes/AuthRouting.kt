package ru.students.routes

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.students.requests.UserCredentials
import ru.students.services.AuthService

fun Application.authRouting() {
    routing {
        route("/api/auth") {
            post("/login") {
                val credits = call.receive<UserCredentials>()
                val result = AuthService.authenticate(credits)
                call.respond(result.code, result.data ?: result.message)
            }
        }
    }
}