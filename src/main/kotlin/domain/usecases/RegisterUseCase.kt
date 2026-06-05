package dev.stranik.domain.usecases

import dev.stranik.domain.model.User
import dev.stranik.domain.repository.UserRepository
import dev.stranik.security.PasswordHasher

class RegisterUseCase(
    private val userRepository: UserRepository,
    private val passwordHasher: PasswordHasher
) {
    suspend fun register(user: User): Boolean {
        val hashed = passwordHasher.hash(user.passwordHash)

        val saveUser = user.copy(passwordHash = hashed)

        userRepository.findByEmail(saveUser.email)?.let {
            return false
        }

        val result = userRepository.addUser(saveUser)
        return result != null
    }
}