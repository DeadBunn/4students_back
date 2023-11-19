package ru.students.models.ad

import org.ktorm.entity.Entity

interface Tag : Entity<Tag> {
    companion object : Entity.Factory<Tag>()

    val id: Long
    val name: String
    val useCount: Int
}