package ru.students.models.ad

import org.ktorm.schema.*
import ru.students.models.user.Users

object Ads : Table<Ad>("ads") {
    val id = long("ad_id").primaryKey().bindTo { it.id }
    val title = varchar("title").bindTo { it.title }
    val description = varchar("description").bindTo { it.description }
    val creationDate = date("creation_date").bindTo { it.creationDate }
    val price = int("price").bindTo { it.price }
    val type = enum<AdType>("type").bindTo { it.type }
    val userId = long("user_id").references(Users) { it.user }
    
}