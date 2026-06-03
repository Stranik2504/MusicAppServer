package dev.stranik.data.databases

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

object ArtistsTable : LongIdTable("artists") {
    val userId = reference("user_id", UserTable.id, ReferenceOption.CASCADE)
    val name = varchar("name", 255).uniqueIndex()
    val bio = varchar("bio", 512)
    val avatarUrl = text("avatar_url")
    val country = varchar("country", 255)
    val monthlyListeners = integer("monthly_listeners")
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
}