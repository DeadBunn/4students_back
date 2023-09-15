package ru.students.plugins

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import ru.vibelab.utils.TokenManager

fun Application.configureSecurity() {
    authentication {
        jwt {
            realm = "user-access"
            verifier(
                TokenManager.verifyAccessToken()
            )
            validate { credential ->
                if (credential.payload.getClaim("userId").asString().isNotEmpty()) {
                    JWTPrincipal(credential.payload)
                } else null
            }
        }
    }
}