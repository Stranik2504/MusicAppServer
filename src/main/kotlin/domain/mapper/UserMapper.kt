package dev.stranik.domain.mapper

import dev.stranik.data.databases.UserTable.passwordHash
import dev.stranik.data.dto.UserDto
import dev.stranik.data.dto.UserInfoDto
import dev.stranik.domain.model.User
import dev.stranik.security.PasswordHasher
import java.time.LocalDateTime

fun User.toUserInfoDto() = UserInfoDto(
    username = username,
    email = email,
    avatarUrl = avatarUrl,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun UserInfoDto.toUser(id: Long) = User(
    id = id,
    username = username,
    passwordHash = "",
    email = email,
    avatarUrl = avatarUrl,
    createdAt = createdAt,
    updatedAt = updatedAt
)