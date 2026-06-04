package dev.stranik.data.repository

import dev.stranik.domain.model.ListeningHistory
import dev.stranik.domain.repository.ListeningHistoryRepository

class ListeningHistoryRepositoryImpl : ListeningHistoryRepository {
    override suspend fun getHistoryByUserId(userId: Long): List<ListeningHistory> {
        TODO("Not yet implemented")
    }

    override suspend fun addListeningHistory(listeningHistory: ListeningHistory): ListeningHistory? {
        TODO("Not yet implemented")
    }
}