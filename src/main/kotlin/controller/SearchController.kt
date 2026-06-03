package dev.stranik.controller

import io.ktor.server.application.Application
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

class SearchController {
    fun configure(application: Application) {
        application.routing {
            get("/") {

            }

            get("/suggestions?q={query}") {
                val query = call.parameters["query"]!!
            }
        }
    }
}