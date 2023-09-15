package ru.students.models.user

import org.ktorm.schema.Table
import org.ktorm.schema.long
import org.ktorm.schema.varchar
import ru.students.models.user.User

object Users : Table<User>("user") {
    val id = long("user_id").primaryKey().bindTo { it.id }
    val etuId = long("etu_id").bindTo { it.etuId }
    val email = varchar("email").bindTo { it.email }
    val password = varchar("password").bindTo { it.password }
}