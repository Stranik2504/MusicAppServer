package dev.stranik.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(val username: String, val password: String)