package ru.students.models.ad

import org.ktorm.entity.Entity

interface AdTag:Entity<AdTag> {
    companion object : Entity.Factory<AdTag>()

    val ad: Ad
    val tag: Tag
}