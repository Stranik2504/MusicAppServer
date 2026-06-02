package dev.stranik.domain.mapper

import dev.stranik.data.dto.UserDto
import dev.stranik.domain.model.User
import dev.stranik.security.PasswordHasher

fun UserDto.toUser(passwordHasher: PasswordHasher) = User(
    id = -1,
    username = username,
    email = email,
    passwordHash = passwordHasher.hash(password)
)