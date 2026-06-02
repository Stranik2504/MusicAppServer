package dev.stranik.domain.usecases

import dev.stranik.domain.repository.UserRepository
import dev.stranik.security.JwtConfig
import dev.stranik.security.PasswordHasher

class LoginUseCase(
    private val userRepository: UserRepository,
    private val passwordHasher: PasswordHasher
) {
    suspend fun login(username: String, password: String): String? {
        val user = userRepository.findByEmail(username) ?: return null

        if (!passwordHasher.verify(password, user.passwordHash)) return null

        return JwtConfig.generateToken(user.username, user.email)
    }
}