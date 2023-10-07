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
                if (credential.payload.getClaim("userId").toString().isNotEmpty()) {
                    JWTPrincipal(credential.payload)
                } else null
            }
        }
        jwt("admin") {
            realm = "admin-access"
            verifier(
                TokenManager.verifyAccessToken()
            )
            validate { credential ->
                val userId = credential.payload.getClaim("userId").toString()
                val roleClaim = credential.payload.getClaim("role").asString()
                if (userId.isNotEmpty() && roleClaim == "ADMIN"
                ) {
                    JWTPrincipal(credential.payload)
                } else null
            }
        }
        jwt("moderator") {
            realm = "moderator-access"
            verifier(
                TokenManager.verifyAccessToken()
            )
            validate { credential ->
                val userId = credential.payload.getClaim("userId").toString()
                val roleClaim = credential.payload.getClaim("role").asString()
                if (userId.isNotEmpty() && (roleClaim == "ADMIN" || roleClaim == "MODERATOR")
                ) {
                    JWTPrincipal(credential.payload)
                } else null
            }
        }
    }
}