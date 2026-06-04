package dev.stranik.data.repository

import dev.stranik.data.databases.UserTable
import dev.stranik.domain.model.User
import dev.stranik.domain.repository.UserRepository
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.time.LocalDateTime

class UserRepositoryImpl : UserRepository {
    override suspend fun addUser(user: User): User? {
        return transaction {
            val emailExists = UserTable.selectAll()
                .where { UserTable.email eq user.email }
                .singleOrNull() != null

            val usernameExists = UserTable.selectAll()
                .where { UserTable.username eq user.username }
                .singleOrNull() != null

            if (emailExists || usernameExists) {
                return@transaction null
            }

            val id = UserTable.insertAndGetId {
                it[username] = user.username
                it[email] = user.email
                it[passwordHash] = user.passwordHash
                it[avatarUrl] = user.avatarUrl
            }

            findByIdInternal(id.value)
        }
    }

    override suspend fun findByEmail(email: String): User? = transaction {
        UserTable
            .selectAll()
            .where { UserTable.email eq email }
            .singleOrNull()
            ?.toUser()
    }

    override suspend fun findById(id: Long): User? = transaction {
        findByIdInternal(id)
    }

    override suspend fun updateUser(user: User): Boolean {
        return transaction {
            val existing = findByIdInternal(user.id) ?: return@transaction false

            val passwordHash = if (user.passwordHash.isBlank()) existing.passwordHash else user.passwordHash
            val now = LocalDateTime.now()

            UserTable.update({ UserTable.id eq user.id }) {
                it[username] = user.username
                it[email] = user.email
                it[UserTable.passwordHash] = passwordHash
                it[avatarUrl] = user.avatarUrl
                it[updatedAt] = now
            } > 0
        }
    }

    private fun findByIdInternal(id: Long): User? {
        return UserTable
            .selectAll()
            .where { UserTable.id eq id }
            .singleOrNull()
            ?.toUser()
    }

    private fun ResultRow.toUser(): User = User(
        id = this[UserTable.id].value,
        username = this[UserTable.username],
        email = this[UserTable.email],
        passwordHash = this[UserTable.passwordHash],
        avatarUrl = this[UserTable.avatarUrl],
        createdAt = this[UserTable.createdAt],
        updatedAt = this[UserTable.updatedAt],
    )
}