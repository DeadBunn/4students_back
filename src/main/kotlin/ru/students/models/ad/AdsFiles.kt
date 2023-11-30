package ru.students.models.ad

import org.ktorm.schema.Table
import org.ktorm.schema.long
import ru.students.models.file.Files

object AdsFiles : Table<AdFile>("ads_files") {

    val fileId = long("file_id").references(Files) { it.file }
    val adId = long("ad_id").references(Ads) { it.ad }
}