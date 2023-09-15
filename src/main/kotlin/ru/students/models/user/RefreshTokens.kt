package ru.students.models.user

import org.ktorm.schema.Table
import org.ktorm.schema.long
import org.ktorm.schema.varchar

object RefreshTokens: Table<Nothing>("refresh_token") {
    val userId = long("user_id").primaryKey()
    val token = varchar("token")
}