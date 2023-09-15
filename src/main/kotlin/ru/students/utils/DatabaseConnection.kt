package ru.vibelab.utils

import io.ktor.server.config.*
import org.ktorm.database.Database

object DatabaseConnection {

    private var database: Database? = null

    fun connect(config: HoconApplicationConfig) {
        val url = config.property("ktor.db.url").getString()
        val user = config.property("ktor.db.user").getString()
        val password = config.property("ktor.db.password").getString()

        database = Database.connect(
            url = url,
            user = user,
            password = password
        )
    }

    fun getDatabase(): Database {
        return requireNotNull(database) { "Database is not initialized. Call connect() first." }
    }
}
