package dev.stranik.data.databases

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

object PlaylistTracksTable : LongIdTable("playlist_tracks") {
    val playlistId = reference("playlist_id", PlaylistsTable.id, ReferenceOption.CASCADE)
    val trackId = reference("track_id", TracksTable.id, ReferenceOption.CASCADE)
    val position = integer("position")
    val addedAt = datetime("added_at").defaultExpression(CurrentDateTime)
}