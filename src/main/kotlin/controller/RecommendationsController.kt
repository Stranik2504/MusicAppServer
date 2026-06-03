package dev.stranik.controller

import io.ktor.server.application.Application
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

class RecommendationsController {
    fun configure(application: Application) {
        application.routing {
            get("/home") {

            }

            get("/for-track/{trackId}") {
                val trackId = call.parameters["trackId"]
            }

            get("/for-artist/{artistId}") {
                val artistId = call.parameters["artistId"]
            }
        }
    }
}