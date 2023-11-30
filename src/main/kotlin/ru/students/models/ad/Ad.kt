package ru.students.models.ad

import org.ktorm.dsl.eq
import org.ktorm.entity.Entity
import ru.students.models.getList
import ru.students.models.user.User
import java.time.LocalDate

interface Ad : Entity<Ad> {

    companion object : Entity.Factory<Ad>()

    val id: Long
    val title: String
    val description: String
    val creationDate: LocalDate
    val price: Int
    val type: AdType
    val user: User
    val tags get() = AdsTags.getList { it.adId eq id }.map { it.tag }
    val files get() = AdsFiles.getList { it.adId eq id }.map { it.file }

}