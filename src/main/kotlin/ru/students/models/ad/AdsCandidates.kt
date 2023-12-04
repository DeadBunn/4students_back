package ru.students.models.ad

import org.ktorm.schema.Table
import org.ktorm.schema.long
import ru.students.models.user.Users

object AdsCandidates : Table<AdCandidate>("ads_candidates") {

    val adId = long("ad_id").references(Ads) { it.ad }
    val candidateId = long("candidate_id").references(Users) { it.candidate }
}