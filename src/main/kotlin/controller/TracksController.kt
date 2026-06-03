package dev.stranik.controller

import io.ktor.server.application.Application
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing

class TracksController {
    fun configure(application: Application) {
        application.routing {
            get("/") {

            }

            get("/{trackId}") {
                val trackId = call.parameters["trackId"]!!
            }

            get("/{trackId}/stream") {
                val trackId = call.parameters["trackId"]!!
            }

            get("/{trackId}/hls") {
                val trackId = call.parameters["trackId"]!!
            }

            post("/{trackId}/like") {
                val trackId = call.parameters["trackId"]!!
            }

            delete("/{trackId}/like") {
                val trackId = call.parameters["trackId"]!!
            }

            get("/{trackId}/likes") {
                val trackId = call.parameters["trackId"]!!
            }
        }
    }
}