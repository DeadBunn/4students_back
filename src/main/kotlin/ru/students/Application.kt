package ru.students

import com.typesafe.config.ConfigFactory
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.cors.routing.*
import ru.students.plugins.*
import ru.vibelab.utils.DatabaseConnection
import ru.vibelab.utils.PasswordEncryptor

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {
    configureSecurity()
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
        allowHeader(HttpHeaders.AccessControlAllowOrigin)
        exposeHeader(HttpHeaders.AccessControlAllowOrigin)
        allowHeader(HttpHeaders.ContentType)
        exposeHeader(HttpHeaders.ContentType)
        allowCredentials = true
        anyHost()
    }
    configureSerialization()
    configureRouting()
    configureFlyway()
    configureSwagger()

    val config = HoconApplicationConfig(ConfigFactory.load())
    DatabaseConnection.connect(config)
    PasswordEncryptor.init(config)
}