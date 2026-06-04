package dev.stranik.domain.mapper

import dev.stranik.data.databases.UserTable.passwordHash
import dev.stranik.data.dto.ListeningHistoryDto
import dev.stranik.data.dto.UserDto
import dev.stranik.data.dto.UserInfoDto
import dev.stranik.domain.model.ListeningHistory
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

fun ListeningHistory.toListeningHistoryDto() = ListeningHistoryDto(
    trackId = trackId,
    playedSec = playedSec,
    playedAt = playedAt
)

fun ListeningHistoryDto.toListeningHistory(userId: Long) = ListeningHistory(
    id = -1,
    userId = userId,
    trackId = trackId,
    playedSec = playedSec,
    playedAt = playedAt
)