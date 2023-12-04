package ru.students.models.ad

import org.ktorm.entity.Entity
import ru.students.models.user.User

interface AdCandidate : Entity<AdCandidate> {

    companion object : Entity.Factory<AdCandidate>()

    val candidate: User
    val ad: Ad

}