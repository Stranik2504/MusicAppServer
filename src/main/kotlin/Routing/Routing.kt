package dev.stranik.Routing

import at.favre.lib.crypto.bcrypt.BCrypt
import dev.stranik.di.AppContainer
import io.ktor.server.application.*
import io.ktor.server.auth.authenticate
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/ping") {
            // BCrypt.withDefaults().hashToString(12, password.toCharArray())
            val password = "12345"
            val hash = "\$2a\$12\$ryg0VwPbN7qqKx1tMZHfveoL7Ie//M1wnKVkT/PCPDrSrvJrWhuzC"
            val res = BCrypt.verifyer().verify(password.toCharArray(), hash).verified
            call.respondText("Pong!" + res)
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