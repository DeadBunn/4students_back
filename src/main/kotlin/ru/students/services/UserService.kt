package ru.students.services

import io.ktor.http.*
import ru.students.dtos.BaseResponse
import ru.students.repos.UserRepo

object UserService {

    fun replenishBalance(userId: Long, sum: Int) {
        UserRepo.replenishBalance(userId, sum)
    }

    fun withDrawBalance(userId: Long, sum: Int): BaseResponse<String> {
        val user = UserRepo.findUserById(userId)
            ?: return BaseResponse(message = "Пользователь не найден", code = HttpStatusCode.NotFound)

        if (user.balance < sum) {
            return BaseResponse(message = "На счету недостаточно средств: ${user.balance}")
        }
        UserRepo.withDrawBalance(userId, sum)

        return BaseResponse(data = "Деньги успешно списаны")
    }
}