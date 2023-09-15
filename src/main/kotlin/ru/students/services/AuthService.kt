package ru.students.services

import io.ktor.http.*
import ru.students.dtos.BaseResponse
import ru.students.dtos.JWTResponse
import ru.students.repos.UserRepo
import ru.students.requests.UserCredentials
import ru.vibelab.utils.PasswordEncryptor
import ru.vibelab.utils.TokenManager

object AuthService {

    fun authenticate(credentials: UserCredentials): BaseResponse<JWTResponse> {
        val user = UserRepo.findUserByEmail(credentials.email)
            ?: return BaseResponse(
                message = "Неправильный логин или пароль",
                code = HttpStatusCode.Unauthorized
            )

        return if (user.password == PasswordEncryptor.hash(credentials.password)) {
            val refreshToken = TokenManager.generateRefreshToken(user)
            UserRepo.setRefreshToken(user, refreshToken)
            BaseResponse(
                data = JWTResponse(
                    accessToken = TokenManager.generateAccessToken(user),
                    refreshToken = refreshToken
                )
            )
        } else BaseResponse(
            message = "Неправильный логин или пароль",
            code = HttpStatusCode.Unauthorized
        )
    }
}