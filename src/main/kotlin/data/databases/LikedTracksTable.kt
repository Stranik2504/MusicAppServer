package dev.stranik.data.databases

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

object LikedTracksTable : LongIdTable("liked_tracks") {
    val userId = reference("user_id", UserTable.id, ReferenceOption.CASCADE)
    val trackId = reference("track_id", TracksTable.id, ReferenceOption.CASCADE)
    val likedAt = datetime("liked_at").defaultExpression(CurrentDateTime)
}