package dev.stranik.data.databases

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

object TracksTable : LongIdTable("tracks") {
    val albumId = reference("album_id", AlbumsTable.id, ReferenceOption.CASCADE).nullable()
    val artistId = reference("artist_id", ArtistsTable.id, ReferenceOption.CASCADE).nullable()
    val title = varchar("title", 255)
    val durationSec = integer("duration_sec")
    val fileUrl = text("file_url")
    val trackNumber = integer("track_number")
    val playCount = integer("play_count")
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
}