package ru.students.repos

import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.insert
import org.ktorm.dsl.insertAndGenerateKey
import org.ktorm.entity.filter
import org.ktorm.entity.first
import org.ktorm.entity.firstOrNull
import org.ktorm.entity.sequenceOf
import ru.students.models.file.FileEntity
import ru.students.models.file.Files
import ru.vibelab.utils.DatabaseConnection

object FileRepo {
    private val db = DatabaseConnection.getDatabase()
    fun adFile(name: String, filePath: String): Long {
        return db.insertAndGenerateKey(Files) {
            set(it.name, name)
            set(it.filePath, filePath)
        }.toString().toLong()
    }

    fun findById(id: Long): FileEntity? {
        return db.sequenceOf(Files)
            .filter { it.id eq id }
            .firstOrNull()
    }
}