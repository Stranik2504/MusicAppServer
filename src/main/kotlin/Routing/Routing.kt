package dev.stranik.Routing

import dev.stranik.di.AppContainer
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    val context = this

    routing {
        get("/") {
            call.respondText("Hello, World!")
        }

        route("/auth") {
            AppContainer.authController.configure(context)
        }
    }
}