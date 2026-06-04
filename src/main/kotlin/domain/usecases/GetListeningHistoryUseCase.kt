package dev.stranik.domain.usecases

import dev.stranik.data.dto.ListeningHistoryDto
import dev.stranik.data.dto.UserInfoDto
import dev.stranik.domain.mapper.toListeningHistoryDto
import dev.stranik.domain.mapper.toUserInfoDto
import dev.stranik.domain.repository.ListeningHistoryRepository
import dev.stranik.domain.repository.UserRepository

class GetListeningHistoryUseCase(
    private val listeningHistoryRepository: ListeningHistoryRepository
) {
    suspend fun invoke(id: Long): List<ListeningHistoryDto> {
        val listHistories = listeningHistoryRepository.getHistoryByUserId(id)

        return listHistories.map { it.toListeningHistoryDto() }
    }
}