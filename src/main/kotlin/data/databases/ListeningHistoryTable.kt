package dev.stranik.data.databases

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

object ListeningHistoryTable : LongIdTable("listening_history") {
    val userId = reference("user_id", UserTable.id, ReferenceOption.CASCADE)
    val trackId = reference("track_id", TracksTable.id, ReferenceOption.CASCADE)
    val playedSec = integer("played_sec")
    val playedAt = datetime("played_at").defaultExpression(CurrentDateTime)
}