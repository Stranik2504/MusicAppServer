package dev.stranik.domain.repository

interface GenresRepository {
    suspend fun findAll(): List<String>
}