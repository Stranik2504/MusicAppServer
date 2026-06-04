package dev.stranik.domain.usecases

import dev.stranik.domain.model.ListeningHistory
import dev.stranik.domain.repository.ListeningHistoryRepository

class AddListeningHistoryUseCase(
    private val listeningHistoryRepository: ListeningHistoryRepository
) {
    suspend fun invoke(listeningHistory: ListeningHistory): Boolean {
        val result = listeningHistoryRepository.addListeningHistory(listeningHistory)

        return result != null
    }
}