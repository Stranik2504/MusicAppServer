package dev.stranik.data.databases

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

object PlaylistsTable : LongIdTable("playlists") {
    val userId = reference("user_id", UserTable.id, ReferenceOption.CASCADE)
    val title = varchar("title", 255)
    val coverUrl = text("cover_url")
    val isPublic = bool("is_public")
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
    val updatedAt = datetime("updated_at").defaultExpression(CurrentDateTime)
}