package ru.students.services

import io.ktor.http.content.*
import ru.students.repos.FileRepo
import java.io.File
import java.util.*

object FileService {
    private const val STORAGE_DIRECTORY = "files"

    init {
        File(STORAGE_DIRECTORY).mkdirs()
    }

    fun saveFile(fileItem: PartData.FileItem): Long {
        val name = fileItem.originalFileName!!
        val filePath = UUID.randomUUID().toString() + "." + getFileExtension(name)
        val savePath = "$STORAGE_DIRECTORY/$filePath"
        val outputFile = File(savePath)
        fileItem.streamProvider().use { its ->
            outputFile.outputStream().buffered().use {
                its.copyTo(it)
            }
        }
        return FileRepo.adFile(name, filePath)
    }

    private fun getFileExtension(fileName: String): String? {
        val file = File(fileName)
        val dotIndex = file.name.lastIndexOf('.')

        return if (dotIndex > 0 && dotIndex < file.name.length - 1) {
            file.name.substring(dotIndex + 1)
        } else {
            null
        }
    }

    fun getFileById(fileId: Long): Pair<File, String>? {

        val fileEntity = FileRepo.findById(fileId) ?: return null

        val filePath = "$STORAGE_DIRECTORY/${fileEntity.filePath}"
        val file = File(filePath)

        return if (file.exists()) {
            Pair(file, fileEntity.name)
        } else {
            null
        }
    }
}