package ru.students.repos

import io.ktor.http.cio.internals.*
import org.ktorm.dsl.eq
import org.ktorm.dsl.insert
import org.ktorm.dsl.insertAndGenerateKey
import org.ktorm.entity.find
import org.ktorm.entity.sequenceOf
import org.ktorm.entity.toList
import ru.students.dtos.requests.CreateAdRequest
import ru.students.models.ad.Ad
import ru.students.models.ad.Ads
import ru.students.models.ad.AdsTags
import ru.vibelab.utils.DatabaseConnection
import java.time.LocalDate

object AdRepo {
    private val db = DatabaseConnection.getDatabase()
    fun getAdsList(): List<Ad> {
        return db.sequenceOf(Ads).toList()
    }

    fun createAd(request: CreateAdRequest, userId: Long): Long {
        val id = db.insertAndGenerateKey(Ads) {
            set(it.creationDate, LocalDate.now())
            set(it.type, request.type)
            set(it.description, request.description)
            set(it.userId, userId)
            set(it.price, request.price)
            set(it.title, request.title)
        }.toString().parseDecLong()

        request.tags.forEach { tag ->
            db.insert(AdsTags) {
                set(it.adId, id)
                set(it.tagId, tag)
            }
        }

        return id
    }

    fun findAdById(id: Long): Ad? {
        return db.sequenceOf(Ads)
            .find { it.id eq id }
    }
}