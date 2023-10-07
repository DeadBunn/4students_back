package ru.students.models.user

import org.ktorm.entity.Entity


interface User : Entity<User> {
    companion object : Entity.Factory<User>()

    val id: Long
    val login: String
    val email: String
    val password: String
    val role: Role
}