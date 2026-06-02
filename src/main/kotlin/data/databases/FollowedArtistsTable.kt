package dev.stranik.data.databases

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

object FollowedArtistsTable : LongIdTable("followed_artists") {
    val userId = reference("user_id", UserTable.id, ReferenceOption.CASCADE)
    val artistId = reference("artist_id", ArtistsTable.id, ReferenceOption.CASCADE)
    val followedAt = datetime("followed_at").defaultExpression(CurrentDateTime)
}