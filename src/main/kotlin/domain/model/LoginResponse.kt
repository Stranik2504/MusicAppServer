package dev.stranik.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(val token: String, val expiration: Long)