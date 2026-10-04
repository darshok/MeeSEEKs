package com.glootie.meeseeks.domain.model

data class CharacterDetails(
    val name: String,
    val status: CharacterStatus,
    val species: String,
    val gender: String,
    val origin: String,
    val location: String,
    val image: String?
)

enum class CharacterStatus {
    ALIVE,
    DEAD,
    UNKNOWN
}