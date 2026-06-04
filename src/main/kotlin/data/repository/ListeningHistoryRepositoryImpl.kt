package dev.stranik.data.repository

import dev.stranik.data.databases.ListeningHistoryTable
import dev.stranik.domain.model.ListeningHistory
import dev.stranik.domain.repository.ListeningHistoryRepository
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class ListeningHistoryRepositoryImpl : ListeningHistoryRepository {
    override suspend fun getHistoryByUserId(userId: Long): List<ListeningHistory> = transaction {
        ListeningHistoryTable
            .selectAll()
            .where { ListeningHistoryTable.userId eq userId }
            .orderBy(ListeningHistoryTable.playedAt to SortOrder.DESC, ListeningHistoryTable.id to SortOrder.DESC)
            .map { it.toListeningHistory() }
    }

    override suspend fun addListeningHistory(listeningHistory: ListeningHistory): ListeningHistory? = transaction {
        val inserted = ListeningHistoryTable.insert {
            it[userId] = listeningHistory.userId
            it[trackId] = listeningHistory.trackId
            it[playedSec] = listeningHistory.playedSec
            it[playedAt] = listeningHistory.playedAt
        }.resultedValues?.singleOrNull() ?: return@transaction null

        inserted.toListeningHistory()
    }

    private fun ResultRow.toListeningHistory() = ListeningHistory(
        id = this[ListeningHistoryTable.id].value,
        userId = this[ListeningHistoryTable.userId].value,
        trackId = this[ListeningHistoryTable.trackId].value,
        playedSec = this[ListeningHistoryTable.playedSec],
        playedAt = this[ListeningHistoryTable.playedAt],
    )
}