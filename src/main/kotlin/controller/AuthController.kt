package dev.stranik.controller

import dev.stranik.domain.model.LoginRequest
import dev.stranik.domain.model.LoginResponse
import dev.stranik.domain.model.User
import dev.stranik.domain.usecases.LoginUseCase
import dev.stranik.domain.usecases.RegisterUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.request.receive
import io.ktor.server.request.receiveParameters
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.post
import io.ktor.server.routing.routing


class AuthController(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase
) {
    fun configure(application: Application) {
        application.routing {
            post("/register") {
                val request = call.receive<User>()

                val created = registerUseCase.register(request)

                if (created == null) {
                    call.respondText("User already exists", status = HttpStatusCode.Conflict)
                } else {
                    call.respondText("OK", status = HttpStatusCode.Created)
                }
            }

            post("/login") {
                val request = call.receive<LoginRequest>()
                val token = loginUseCase.login(request.username, request.password)

                if (token != null) {
                    call.respond(LoginResponse(token, 100))
                } else {
                    call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "Неверный логин или пароль"))
                }
            }
        }
    }
}

/*
fun Application.configureAuthRoutes() {
    routing {
        route("/auth") {
            post("/register") {
                val params = call.receiveParameters()
                val username = params["username"] ?: return@post call.respondText(
                    "Missing 'username' parameter",
                    status = HttpStatusCode.BadRequest
                )
                val password = params["password"] ?: return@post call.respondText(
                    "Missing 'password' parameter",
                    status = HttpStatusCode.BadRequest
                )

                val created = authService.register(username, password)
                if (!created) {
                    call.respondText("User already exists", status = HttpStatusCode.Conflict)
                } else {
                    call.respondText("OK", status = HttpStatusCode.Created)
                }
            }

            post("/login") {
                val params = call.receiveParameters()
                val username = params["username"] ?: return@post call.respondText(
                    "Missing 'username' parameter",
                    status = HttpStatusCode.BadRequest
                )
                val password = params["password"] ?: return@post call.respondText(
                    "Missing 'password' parameter",
                    status = HttpStatusCode.BadRequest
                )

                val ok = authService.authenticate(username, password)
                if (!ok) return@post call.respondText("Invalid credentials", status = HttpStatusCode.Unauthorized)

                val token = JwtConfig.generateToken(username)
                call.respondText("{\"token\":\"$token\"}", ContentType.Application.Json)
            }
        }

        authenticate("auth-jwt") {
            get("/protected") {
                val principal = call.principal<JWTPrincipal>()
                val username = principal?.payload?.getClaim("username")?.asString()
                call.respondText("Hello, $username. You accessed a protected endpoint.")
            }
        }
    }
}*/
