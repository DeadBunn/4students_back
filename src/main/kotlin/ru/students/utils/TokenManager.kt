package ru.vibelab.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.typesafe.config.ConfigFactory
import io.ktor.server.config.*
import ru.students.models.user.User
import java.time.Instant
import java.time.temporal.ChronoUnit

object TokenManager {
    private val config = HoconApplicationConfig(ConfigFactory.load())
    private val audience = config.property("jwt.audience").getString()
    private val issuer = config.property("jwt.issuer").getString()
    private val accessSecret = config.property("jwt.secret.access").getString()
    private val refreshSecret = config.property("jwt.secret.refresh").getString()
    private val accessExpires = config.property("jwt.expires.access-sec").getString().toLong()
    private val refreshExpires = config.property("jwt.expires.refresh-day").getString().toLong()

    fun generateAccessToken(user: User): String {
        return JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withClaim("userId", user.id)
            .withExpiresAt(Instant.now().plusSeconds(accessExpires))
            .sign(Algorithm.HMAC256(accessSecret))
    }

    fun generateRefreshToken(user: User): String {
        return JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withClaim("userId", user.id)
            .withExpiresAt(Instant.now().plus(refreshExpires, ChronoUnit.DAYS))
            .sign(Algorithm.HMAC256(refreshSecret))
    }

    fun verifyAccessToken(): JWTVerifier {
        return JWT.require(Algorithm.HMAC256(accessSecret))
            .withAudience(audience)
            .withIssuer(issuer)
            .build()
    }

    fun verifyRefreshToken(): JWTVerifier {
        return JWT.require(Algorithm.HMAC256(refreshSecret))
            .withAudience(audience)
            .withIssuer(issuer)
            .build()
    }
}
