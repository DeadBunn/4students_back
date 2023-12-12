package ru.students.services

import io.ktor.http.*
import ru.students.dtos.BaseResponse
import ru.students.dtos.JWTResponse
import ru.students.repos.UserRepo
import ru.students.requests.RegisterUser
import ru.vibelab.utils.PasswordEncryptor
import ru.vibelab.utils.TokenManager

object RegisterService {

    fun registerUser(info: RegisterUser): BaseResponse<JWTResponse> {

        if (UserRepo.findUserByEmail(info.email.lowercase()) != null) {
            return BaseResponse(
                code = HttpStatusCode.BadRequest,
                message = "Пользователь с данной почтой уже зарегистрирован"
            )
        }

        if (UserRepo.findUserByLogin(info.login) != null) {
            return BaseResponse(
                code = HttpStatusCode.BadRequest,
                message = "Пользователь с данным логином уже зарегистрирован"
            )
        }

        val user = UserRepo.addUser(
            info.email.lowercase(),
            info.login,
            PasswordEncryptor.hash(info.password)
        ) ?: return BaseResponse(
            code = HttpStatusCode.InternalServerError,
            message = "Ошибка создания пользователя"
        )

        val refreshToken = TokenManager.generateRefreshToken(user)
        UserRepo.setRefreshToken(user.id, refreshToken)
        return BaseResponse(
            data = JWTResponse(
                accessToken = TokenManager.generateAccessToken(user),
                refreshToken = refreshToken,
                id = user.id
            )
        )
    }
}