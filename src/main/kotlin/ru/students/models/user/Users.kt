package ru.students.models.user

import org.ktorm.schema.*

object Users : Table<User>("user") {
    val id = long("user_id").primaryKey().bindTo { it.id }
    val login = varchar("login").bindTo { it.login }
    val email = varchar("email").bindTo { it.email }
    val password = varchar("password").bindTo { it.password }
    val role = enum<Role>("role").bindTo { it.role }
    val balance = int("balance").bindTo { it.balance }
}