package ru.students.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*

fun Application.configureHTTP() {
    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowHeader(HttpHeaders.Authorization)
        exposeHeader(HttpHeaders.Authorization)
        allowHeader(HttpHeaders.AccessControlAllowHeaders)
        exposeHeader(HttpHeaders.AccessControlAllowHeaders)
        allowCredentials = true
        anyHost()
        println("Cors has been configured")
    }
}
