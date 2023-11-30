package ru.students.models.ad

import org.ktorm.entity.Entity
import ru.students.models.file.FileEntity

interface AdFile : Entity<AdFile> {

    companion object : Entity.Factory<AdFile>()

    val file: FileEntity
    val ad: Ad
}