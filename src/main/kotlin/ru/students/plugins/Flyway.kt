package ru.students.plugins

import io.ktor.server.application.*
import org.flywaydb.core.Flyway

fun Application.configureFlyway(){
    val url = environment.config.property("ktor.db.url").getString()
    val user = environment.config.property("ktor.db.user").getString()
    val password = environment.config.property("ktor.db.password").getString()

    Flyway.configure().dataSource(url, user, password).load().migrate()
}