package ru.students.models.file

import org.ktorm.schema.Table
import org.ktorm.schema.long
import org.ktorm.schema.varchar

object Files : Table<FileEntity>("files") {

    val id = long("file_id").primaryKey().bindTo { it.id }
    val name = varchar("name").bindTo { it.name }
    val filePath = varchar("file_path").bindTo { it.filePath }
}