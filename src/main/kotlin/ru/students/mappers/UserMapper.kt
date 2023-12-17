package ru.students.mappers

import ru.students.dtos.UserResponse
import ru.students.models.user.User

object UserMapper {

    fun toResponse(user: User): UserResponse {
        return UserResponse(user.id, user.login, 0, user.balance, user.email)
    }
}