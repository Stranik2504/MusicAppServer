package dev.stranik.security

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import java.util.Date
import com.auth0.jwt.algorithms.Algorithm

object JwtConfig {
    // TODO: change secret
    private const val SECRET = "change_this_secret"
    private const val ISSUER = "musicAppIssuer"
    private const val AUDIENCE = "musicAppAudience"
    private val ALGORITHM = Algorithm.HMAC256(SECRET)

    private const val VALIDITY = 7L * 24 * 60 * 60 * 1000 // 7

    fun generateToken(userId: Long, username: String, email: String): String {
        val now = System.currentTimeMillis()
        val exp = Date(now + VALIDITY)

        return JWT.create()
            .withIssuer(ISSUER)
            .withAudience(AUDIENCE)
            .withClaim("userId", userId)
            .withClaim("username", username)
            .withClaim("email", email)
            .withExpiresAt(exp)
            .sign(ALGORITHM)
    }

    fun verifier(): JWTVerifier = JWT
        .require(ALGORITHM)
        .withIssuer(ISSUER)
        .withAudience(AUDIENCE)
        .build()
}