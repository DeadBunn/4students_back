package ru.students.plugins

import io.ktor.server.application.*
import ru.students.routes.*

fun Application.configureRouting() {
    authRouting()
    registerRouting()
    adRouting()
    fileRouting()
    balanceRouting()
    moderatorRouting()
    userRouting()
}
