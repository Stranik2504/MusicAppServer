package dev.stranik.data.databases

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

object AlbumsTable : LongIdTable("albums") {
    val artistId = reference("artist_id", ArtistsTable.id, ReferenceOption.CASCADE)
    val genreId = reference("genre_id", GenresTable.id, ReferenceOption.CASCADE)
    val title = varchar("title", 255).uniqueIndex()
    val coverUrl = text("cover_url").nullable()
    val releaseDate = datetime("release_date").defaultExpression(CurrentDateTime)
    val albumType = varchar("album_type", 255).nullable()
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
}