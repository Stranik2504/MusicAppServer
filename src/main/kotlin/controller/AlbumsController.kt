package dev.stranik.controller

import io.ktor.server.application.Application
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing

class AlbumsController {
    fun configure(application: Application) {
        application.routing {
            get("/") {

            }

            get("/{albumId}") {
                val albumId = call.parameters["albumId"]!!
            }
        }
    }
}