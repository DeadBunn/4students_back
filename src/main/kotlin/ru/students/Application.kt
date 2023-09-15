package ru.students

import com.typesafe.config.ConfigFactory
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.netty.*
import ru.students.plugins.*
import ru.vibelab.utils.DatabaseConnection
import ru.vibelab.utils.PasswordEncryptor

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {
    configureSecurity()
    configureHTTP()
    configureSerialization()
    configureRouting()
    configureFlyway()

    val config = HoconApplicationConfig(ConfigFactory.load())
    DatabaseConnection.connect(config)
    PasswordEncryptor.init(config)
}