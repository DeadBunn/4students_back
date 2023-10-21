package ru.students.repos

import org.ktorm.dsl.*
import org.ktorm.entity.find
import org.ktorm.entity.sequenceOf
import ru.students.models.user.RefreshTokens
import ru.students.models.user.Role
import ru.students.models.user.User
import ru.students.models.user.Users
import ru.vibelab.utils.DatabaseConnection

object UserRepo {
    private val db = DatabaseConnection.getDatabase()

    fun setRefreshToken(userId: Long, token: String) {
        val rowsAffected = db.update(RefreshTokens) {
            where { RefreshTokens.userId eq userId }
            set(RefreshTokens.token, token)
        }

        if (rowsAffected < 1) {
            db.insert(RefreshTokens) {
                set(RefreshTokens.userId, userId)
                set(RefreshTokens.token, token)
            }
        }
    }

    fun findUserByEmail(email: String): User? {
        return db.sequenceOf(Users)
            .find { it.email eq email.lowercase() }
    }

    fun findUserByLogin(login: String): User? {
        return db.sequenceOf(Users)
            .find { it.login eq login }
    }

    fun findUserById(id: Long): User? {
        return db.sequenceOf(Users)
            .find { it.id eq id }
    }

    fun findTokenById(id: Long): String? {
        return db.from(RefreshTokens)
            .select(RefreshTokens.token)
            .where(RefreshTokens.userId eq id)
            .map {
                it[RefreshTokens.token]
            }
            .firstOrNull()
    }

    fun addUser(email: String, login: String, password: String): User? {
        db.insertAndGenerateKey(Users) {
            set(it.email, email)
            set(it.login, login)
            set(it.password, password)
            set(it.role, Role.USER)
        }
        return db.sequenceOf(Users)
            .find { it.email eq email.lowercase() }
    }

}