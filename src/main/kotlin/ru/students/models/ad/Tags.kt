package ru.students.models.ad

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.long
import org.ktorm.schema.varchar

object Tags : Table<Tag>("tags") {
    val id = long("tag_id").primaryKey().bindTo { it.id }
    val name = varchar("name").bindTo { it.name }
    val useCount = int("use_count").bindTo { it.useCount }
}