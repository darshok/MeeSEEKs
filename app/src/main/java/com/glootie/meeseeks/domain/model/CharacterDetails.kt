package com.glootie.meeseeks.domain.model

data class CharacterDetails(
    val name: String,
    val status: CharacterStatus,
    val species: String,
    val gender: String,
    val originLocation: CharacterOriginLocation,
    val lastLocation: CharacterLastLocation,
    val image: String?
)

data class CharacterOriginLocation(
    val id: Int?,
    val name: String
)

data class CharacterLastLocation(
    val id: Int?,
    val name: String
)

enum class CharacterStatus {
    ALIVE,
    DEAD,
    UNKNOWN
}