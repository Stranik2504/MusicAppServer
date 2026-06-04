package dev.stranik.controller

import dev.stranik.data.dto.LoginRequestDto
import dev.stranik.data.dto.LoginResponseDto
import dev.stranik.data.dto.UserDto
import dev.stranik.domain.mapper.toUser
import dev.stranik.domain.usecases.LoginUseCase
import dev.stranik.domain.usecases.RegisterUseCase
import dev.stranik.security.PasswordHasher
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.post


class AuthController(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val passwordHasher: PasswordHasher
) {
    fun configure(route: Route) {
        route.apply {
            post("/register") {
                val user = call.receive<UserDto>()
                val created = registerUseCase.register(user.toUser(passwordHasher))

                if (!created) {
                    call.respondText("User already exists", status = HttpStatusCode.Conflict)
                } else {
                    call.respondText("OK", status = HttpStatusCode.Created)
                }
            }

            post("/login") {
                val request = call.receive<LoginRequestDto>()
                val token = loginUseCase.login(request.username, request.password)

                if (token != null) {
                    call.respond(LoginResponseDto(token))
                } else {
                    call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "Неверный логин или пароль"))
                }
            }
        }
    }
}
