package dev.stranik.Routing

import dev.stranik.di.AppContainer
import io.ktor.server.application.*
import io.ktor.server.auth.authenticate
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    val context = this

    routing {
        get("/ping") {
            call.respondText("Pong!")
        }

        route("/api") {
            route("/auth") {
                AppContainer.authController.configure(context)
            }

            authenticate("auth-jwt") {
                route("/users") {
                    AppContainer.usersController.configure(context)
                }

                route("/artists") {
                    AppContainer.artistsController.configure(context)
                }

                route("/albums") {
                    AppContainer.albumsController.configure(context)
                }

                route("/tracks") {
                    AppContainer.tracksController.configure(context)
                }

                route("/genres") {
                    AppContainer.genresController.configure(context)
                }

                route("/playlists") {
                    AppContainer.playlistsController.configure(context)
                }

                route("/recommendations") {
                    AppContainer.recommendationsController.configure(context)
                }

                route("/search") {
                    AppContainer.searchController.configure(context)
                }
            }
        }
    }
}