package ru.students.services

import com.google.gson.Gson

object GsonParser {

    private val gson = Gson()

    fun <T> parse(cl: Class<T>, value: String): T {
        return gson.fromJson(value, cl)
    }
}