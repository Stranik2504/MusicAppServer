package dev.stranik.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class GenreDto(
    val name: String,
    val slug: String,
)