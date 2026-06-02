package dev.stranik.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class User (
    val username: String,
    val email: String,
    val passwordHash: String,
)