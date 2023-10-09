package ru.students.plugins

import io.github.smiley4.ktorswaggerui.SwaggerUI
import io.github.smiley4.ktorswaggerui.dsl.AuthScheme
import io.github.smiley4.ktorswaggerui.dsl.AuthType
import io.ktor.server.application.*

fun Application.configureSwagger() {
    install(SwaggerUI) {
        swagger {
            swaggerUrl = "swagger-ui"
            forwardRoot = true
        }
        info {
            title = "4Students"
            version = "latest"
            description = "Пример API"
        }
        server {
            url = "http://localhost:8080"
            description = "Development Server"
        }
        securityScheme("jwt") {
            type = AuthType.HTTP
            scheme = AuthScheme.BEARER
        }
    }
}