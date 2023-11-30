package ru.students.repos

import org.ktorm.dsl.insert
import org.ktorm.dsl.insertAndGenerateKey
import ru.students.models.file.Files
import ru.vibelab.utils.DatabaseConnection

object FileRepo {
    private val db = DatabaseConnection.getDatabase()
    fun adFile(name: String, filePath:String): Long {
        return db.insertAndGenerateKey(Files) {
            set(it.name, name)
            set(it.filePath, filePath)
        }.toString().toLong()
    }
}