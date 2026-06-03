package dev.stranik.domain.mapper

import dev.stranik.data.dto.UserDto
import dev.stranik.domain.model.User
import dev.stranik.security.PasswordHasher
import java.time.LocalDateTime

fun UserDto.toUser(passwordHasher: PasswordHasher) = User(
    id = -1,
    username = username,
    email = email,
    passwordHash = passwordHasher.hash(password),
    avatarUrl = null,
    createdAt = LocalDateTime.now(),
    updatedAt = LocalDateTime.now()
)