package dev.stranik.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val username: String,
    val email: String,
    val password: String
)