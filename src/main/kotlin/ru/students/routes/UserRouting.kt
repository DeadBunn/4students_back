package ru.students.routes

import io.github.smiley4.ktorswaggerui.dsl.get
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.students.services.UserService

fun Application.userRouting(){

    routing {
        authenticate {
            get("api/user/profile") {
                val userId: Long = call.principal<JWTPrincipal>()!!.payload.claims["userId"]!!.asLong()
                val result = UserService.getUserProfile(userId)
                call.respond(result.code, result.data ?: result.message)
            }
        }
    }
}