package ru.students.dtos

import io.ktor.http.*

data class BaseResponse<T>(
    val code: HttpStatusCode = HttpStatusCode.OK,
    val data: T? = null,
    val message: String = "Default error message"
)