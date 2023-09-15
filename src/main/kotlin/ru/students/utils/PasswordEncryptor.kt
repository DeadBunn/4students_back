package ru.vibelab.utils

import io.ktor.server.config.*
import io.ktor.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object PasswordEncryptor {
    private const val ALGORITHM = "HmacSHA1"
    private var HMAC_KEY: SecretKeySpec? = null

    fun init(config: HoconApplicationConfig) {
        val secretKey = config.property("ktor.security.password-secret-key").getString()
        val hashKey = hex(secretKey)
        HMAC_KEY = SecretKeySpec(hashKey, secretKey)
    }

    private fun getSecretKey(): SecretKeySpec {
        return requireNotNull(HMAC_KEY) { "Secret key wasn't initialized. Call init first" }
    }

    fun hash(password: String): String {
        val hmac = Mac.getInstance(ALGORITHM)
        hmac.init(getSecretKey())
        return hex(hmac.doFinal(password.toByteArray(Charsets.UTF_8)))
    }
}