package ru.students.repos

import org.ktorm.entity.sequenceOf
import org.ktorm.entity.toList
import ru.students.models.ad.Ad
import ru.students.models.ad.Ads
import ru.vibelab.utils.DatabaseConnection

object AdRepo {
    private val db = DatabaseConnection.getDatabase()
    fun getAdsList(): List<Ad> {
        return db.sequenceOf(Ads).toList()
    }
}