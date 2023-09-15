package ru.students.plugins

import io.ktor.server.application.*
import ru.students.routes.authRouting

fun Application.configureRouting() {
    authRouting()
}
