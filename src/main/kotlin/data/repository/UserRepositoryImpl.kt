package dev.stranik.data.repository

import dev.stranik.domain.model.User
import dev.stranik.domain.repository.UserRepository

class UserRepositoryImpl : UserRepository {
    override suspend fun addUser(user: User): User? {
        TODO("Not yet implemented")
    }

    override suspend fun findByEmail(email: String): User {
        TODO("Not yet implemented")
    }
}