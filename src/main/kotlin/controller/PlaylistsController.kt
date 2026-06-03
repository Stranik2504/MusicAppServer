package dev.stranik.controller

import io.ktor.server.application.Application
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.routing

class PlaylistsController {
    fun configure(application: Application) {
        application.routing {
            get("/") {

            }

            get("/{playlistId}") {
                val playlistId = call.parameters["playlistId"]
            }

            post("/") {

            }

            put("/{playlistId}") {
                val playlistId = call.parameters["playlistId"]
            }

            delete("/{playlistId}") {
                val playlistId = call.parameters["playlistId"]
            }

            post("/{playlistId}/tracks") {
                val playlistId = call.parameters["playlistId"]
            }

            delete("/{playlistId}/tracks/{trackId}") {
                val playlistId = call.parameters["playlistId"]
                val trackId = call.parameters["trackId"]
            }
        }
    }
}