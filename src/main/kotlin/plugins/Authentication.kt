package dev.stranik.plugins

import dev.stranik.security.JwtConfig
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.response.respond

fun Application.configureAuthentication() {
    install(Authentication) {
        jwt("auth-jwt") {
            realm = "ktor-app"

            verifier(JwtConfig.verifier())

            validate { credential ->
                val username = credential.payload.getClaim("username").asString()
                val email = credential.payload.getClaim("email").asString()

                val exp = credential.payload.expiresAt?.time ?: 0
                val now = System.currentTimeMillis()

                if (exp >= now)
                    return@validate null

                if (username == null || username.isBlank())
                    return@validate null

                if (email == null || email.isBlank())
                    return@validate null

                JWTPrincipal(credential.payload)
            }
            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "Invalid or expired token"))
            }
        }
    }
}
