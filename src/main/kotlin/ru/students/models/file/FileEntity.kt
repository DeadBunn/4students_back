package ru.students.models.file

import org.ktorm.entity.Entity

interface FileEntity: Entity<FileEntity> {

    companion object : Entity.Factory<FileEntity>()

    val id: Long
    val name: String
}