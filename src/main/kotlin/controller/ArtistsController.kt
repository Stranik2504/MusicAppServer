package dev.stranik.controller

import io.ktor.server.application.Application
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.routing

class ArtistsController {
    fun configure(application: Application) {
        application.routing {
            post("/{artistId}/follow") {
                val artistId = call.parameters["artistId"]!!
            }

            delete("/{artistId}/follow") {
                val artistId = call.parameters["artistId"]!!

            }

            get("/") {

            }

            get("/{artistId}") {
                val artistId = call.parameters["artistId"]!!
            }


        }
    }
}