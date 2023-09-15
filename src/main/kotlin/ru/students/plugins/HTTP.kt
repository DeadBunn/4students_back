package ru.students.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.routing.*

fun Application.configureHTTP() {
    install(CORS) {
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowHeader(HttpHeaders.Authorization)
        allowHost("teach.vibelab.ru", listOf("http", "https"))
        allowHost("localhost:5001", listOf("http"))
        allowHost("194.85.169.95:9245", listOf("http"))
    }
    routing {
        swaggerUI(path = "openapi")
    }
}
