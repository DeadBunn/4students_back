package ru.students.mappers

import ru.students.dtos.FileResponse
import ru.students.models.file.FileEntity

object FileMapper {

    fun toResponse(file: FileEntity): FileResponse {
        return FileResponse(id = file.id, filePath = file.filePath, name = file.name)
    }
}