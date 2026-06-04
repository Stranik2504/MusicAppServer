package dev.stranik.data.repository

import dev.stranik.data.databases.GenresTable
import dev.stranik.domain.repository.GenresRepository
import org.jetbrains.exposed.sql.selectAll

class GenresRepositoryImpl : GenresRepository {
    override suspend fun findAll(): List<String> {
        return GenresTable.selectAll().map { it[GenresTable.name] }
    }
}