package dev.stranik.Routing

import dev.stranik.di.AppContainer
import io.ktor.server.application.*
import io.ktor.server.auth.authenticate
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/ping") {
            call.respondText("Pong!")
        }

        route("/api") {
            route("/auth") {
                AppContainer.authController.configure(this)
            }

            authenticate("auth-jwt") {
                route("/users") {
                    AppContainer.usersController.configure(this)
                }

                route("/artists") {
                    AppContainer.artistsController.configure(this)
                }

                route("/albums") {
                    AppContainer.albumsController.configure(this)
                }

                route("/tracks") {
                    AppContainer.tracksController.configure(this)
                }

                route("/genres") {
                    AppContainer.genresController.configure(this)
                }

                route("/playlists") {
                    AppContainer.playlistsController.configure(this)
                }

                route("/recommendations") {
                    AppContainer.recommendationsController.configure(this)
                }

                route("/search") {
                    AppContainer.searchController.configure(this)
                }
            }
        }
    }
}