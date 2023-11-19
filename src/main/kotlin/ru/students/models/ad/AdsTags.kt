package ru.students.models.ad

import org.ktorm.schema.Table
import org.ktorm.schema.long

object AdsTags : Table<AdTag>("ads_tags") {
    val adId = long("ad_id").references(Ads) { it.ad }
    val tagId = long("tag_id").references(Tags) { it.tag }
}