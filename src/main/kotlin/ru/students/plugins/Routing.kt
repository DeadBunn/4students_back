package ru.students.plugins

import io.ktor.server.application.*
import ru.students.routes.authRouting
import ru.students.routes.registerRouting

fun Application.configureRouting() {
    authRouting()
    registerRouting()
}
