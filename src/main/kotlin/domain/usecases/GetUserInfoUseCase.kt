package dev.stranik.domain.usecases

import dev.stranik.data.dto.UserInfoDto
import dev.stranik.domain.mapper.toUserInfoDto
import dev.stranik.domain.model.User
import dev.stranik.domain.repository.UserRepository

class GetUserInfoUseCase(
    private val userRepository: UserRepository
) {
    suspend fun execute(id: Long): UserInfoDto? {
        val user = userRepository.findById(id) ?: return null

        return user.toUserInfoDto()
    }
}