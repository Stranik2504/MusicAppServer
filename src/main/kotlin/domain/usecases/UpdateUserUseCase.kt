package dev.stranik.domain.usecases

import dev.stranik.data.dto.UserDto
import dev.stranik.domain.model.User
import dev.stranik.domain.repository.UserRepository

class UpdateUserUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(user: User): Boolean {
        return userRepository.updateUser(user)
    }
}