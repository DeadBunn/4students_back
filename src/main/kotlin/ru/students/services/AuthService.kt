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
        val error = BaseResponse<JWTResponse>(
            message = "Неправильный логин или пароль",
            code = HttpStatusCode.Unauthorized
        )
        val user = UserRepo.findUserByEmail(credentials.email)
            ?: return error

        return if (user.password == PasswordEncryptor.hash(credentials.password)) {
            val refreshToken = TokenManager.generateRefreshToken(user)
            UserRepo.setRefreshToken(user.id, refreshToken)
            BaseResponse(
                data = JWTResponse(
                    accessToken = TokenManager.generateAccessToken(user),
                    refreshToken = refreshToken,
                    id = user.id,
                    login = user.login,
                    email = user.email,
                    role = user.role
                )
            )
        } else error
    }

    fun authenticateRefreshToken(token: String?): BaseResponse<JWTResponse> {
        val error = BaseResponse<JWTResponse>(
            code = HttpStatusCode.BadRequest,
            message = "Ошибка авторизации через refresh-token"
        )

        if (token == null) return error

        val userId = TokenManager
            .verifyRefreshToken()
            .verify(token)
            .getClaim("userId")
            .asLong()
            ?: return error

        val tokenInDb = UserRepo
            .findTokenById(userId)
            ?: return error

        val user = UserRepo.findUserById(userId)
            ?: return error

        return if (token == tokenInDb) {
            val refreshToken = TokenManager.generateRefreshToken(user)
            UserRepo.setRefreshToken(userId, refreshToken)
            BaseResponse(
                data = JWTResponse(
                    accessToken = TokenManager.generateAccessToken(user),
                    refreshToken = refreshToken,
                    id = user.id,
                    login = user.login,
                    email = user.email,
                    role = user.role
                )
            )
        } else {
            return error
        }
    }
}