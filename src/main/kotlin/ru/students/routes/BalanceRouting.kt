package ru.students.routes

import io.github.smiley4.ktorswaggerui.dsl.put
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.students.services.UserService

fun Application.balanceRouting() {
    routing {
        route("api/balance") {
            authenticate {
                put("replenish",
                    {
                        tags = listOf("Баланс")
                        description = "Пополнить баланс"
                        request {
                            pathParameter<Int>("sum") {
                                description = "Сумма, на которую нужно пополнить баланс"
                            }
                        }
                    }) {
                    val sum = call.parameters["sum"]?.toInt()
                    val userId: Long = call.principal<JWTPrincipal>()!!.payload.claims["userId"]!!.asLong()

                    if (sum == null || sum <= 0) {
                        call.respond("Неверная сумма: $sum")
                    } else {
                        UserService.replenishBalance(userId, sum)
                    }

                    call.respond("Баланс успешно пополнен на $sum")
                }

                put("withdraw",
                    {
                        tags = listOf("Баланс")
                        description = "Снять деньги со счета"
                        request {
                            pathParameter<Int>("sum") {
                                description = "Сумма, которую нужно снять с баланса"
                            }
                        }
                    }) {
                    val sum = call.parameters["sum"]?.toInt()
                    val userId: Long = call.principal<JWTPrincipal>()!!.payload.claims["userId"]!!.asLong()

                    if (sum == null || sum <= 0) {
                        call.respond("Неверная сумма: $sum")
                    } else {
                        val result = UserService.withDrawBalance(userId, sum)
                        call.respond(result.code, result.data ?: result.message)
                    }
                }
            }
        }
    }
}