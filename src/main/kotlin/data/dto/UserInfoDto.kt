package dev.stranik.data.dto

import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class UserInfoDto(
    val username: String,
    val email: String,
    val avatarUrl: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)