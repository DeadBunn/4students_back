package ru.students.services

import java.io.File
import java.io.InputStream
import java.util.concurrent.atomic.AtomicLong

object FileService {
    private val fileIdCounter = AtomicLong(1)

    private const val STORAGE_DIRECTORY = "files"

    init {
        File(STORAGE_DIRECTORY).mkdirs()
    }

    fun saveFile(inputStream: InputStream): Long {
        val fileId = fileIdCounter.getAndIncrement()
        val filePath = "$STORAGE_DIRECTORY/$fileId"
        val outputFile = File(filePath)

        // Save the file to the storage directory
        outputFile.outputStream().use { outputStream ->
            inputStream.copyTo(outputStream)
        }

        return fileId
    }

    fun getFileById(fileId: Long): File? {
        val filePath = "$STORAGE_DIRECTORY/$fileId"
        val file = File(filePath)

        return if (file.exists()) {
            file
        } else {
            null
        }
    }
}