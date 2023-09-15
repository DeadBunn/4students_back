package ru.students.repos

import org.ktorm.dsl.eq
import org.ktorm.dsl.insert
import org.ktorm.dsl.update
import org.ktorm.entity.find
import org.ktorm.entity.sequenceOf
import ru.students.models.user.RefreshTokens
import ru.students.models.user.User
import ru.students.models.user.Users
import ru.vibelab.utils.DatabaseConnection

object UserRepo {
    private val db = DatabaseConnection.getDatabase()

    fun setRefreshToken(user: User, token: String) {
        val rowsAffected = db.update(RefreshTokens) {
            where { RefreshTokens.userId eq user.id }
            set(RefreshTokens.token, token)
        }

        if (rowsAffected < 1) {
            db.insert(RefreshTokens) {
                set(RefreshTokens.userId, user.id)
                set(RefreshTokens.token, token)
            }
        }
    }

    fun findUserByEmail(email: String): User? {
        return db.sequenceOf(Users)
            .find { it.email eq email.lowercase() }
    }
}