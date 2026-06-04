package dev.stranik.domain.repository

import dev.stranik.domain.model.ListeningHistory

interface ListeningHistoryRepository {
    suspend fun getHistoryByUserId(userId: Long): List<ListeningHistory>
    suspend fun addListeningHistory(listeningHistory: ListeningHistory): ListeningHistory?
}