package ru.students.routes

import io.github.smiley4.ktorswaggerui.dsl.get
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.students.services.FileService
import java.io.File
import java.nio.file.Files

fun Application.fileRouting() {
    routing {
        route("api/files") {
            get("/{id}", {
                tags = listOf("Файлы")
                description = "Получить файл по id"
            }) {
                val id = call.parameters["id"]?.toLong()
                if (id != null) {
                    val fileWithName = FileService.getFileById(id)

                    if (fileWithName != null && fileWithName.first.exists()) {

                        val disposition = if (isImage(fileWithName.first)) "inline" else "attachment"

                        call.response.header(
                            "Content-Disposition",
                            "$disposition; filename=\"${fileWithName.second}\""
                        )
                        call.respondFile(fileWithName.first)
                    } else {
                        call.respond(HttpStatusCode.NotFound, "Файл не найден")
                    }
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Invalid file id")
                }
            }
        }
    }
}

fun isImage(file: File): Boolean {
    val mimeType = Files.probeContentType(file.toPath())
    return mimeType?.startsWith("image") == true
}