package dev.stranik.data.databases

import org.jetbrains.exposed.dao.id.IntIdTable

object GenresTable : IntIdTable("genres") {
    val name = varchar("name", 255).uniqueIndex()
    val slug = varchar("slug", 255)
}