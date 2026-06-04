package dev.stranik.controller

import io.ktor.server.application.Application
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing

class ArtistsController {
    fun configure(application: Application) {
        application.routing {
            post("/{artistId}/follow") {
                val artistId = call.parameters["artistId"]!!
                val jwtUserId = call.principal<JWTPrincipal>()?.payload?.getClaim("userId")?.asLong()
            }

            delete("/{artistId}/follow") {
                val artistId = call.parameters["artistId"]!!
                val jwtUserId = call.principal<JWTPrincipal>()?.payload?.getClaim("userId")?.asLong()

            }

            get("/") {

            }

            get("/{artistId}") {
                val artistId = call.parameters["artistId"]!!
            }
        }
    }
}