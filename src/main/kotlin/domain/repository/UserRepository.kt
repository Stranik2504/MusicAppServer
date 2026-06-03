package dev.stranik.domain.repository

import dev.stranik.domain.model.User

interface UserRepository {
    suspend fun addUser(user: User): User?
    suspend fun findByEmail(email: String): User?
    suspend fun findById(id: Long): User?
    suspend fun updateUser(user: User): Boolean
}