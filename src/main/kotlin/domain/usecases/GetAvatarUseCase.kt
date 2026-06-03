package dev.stranik.domain.usecases

import dev.stranik.domain.repository.UserRepository

class GetAvatarUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userId: Long): String? {
        val user = userRepository.findById(userId) ?: return null

        return user.avatarUrl
    }
}