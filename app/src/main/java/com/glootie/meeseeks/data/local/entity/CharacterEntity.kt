package com.glootie.meeseeks.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.glootie.meeseeks.domain.model.CharacterDetails
import com.glootie.meeseeks.domain.model.CharacterLastLocation
import com.glootie.meeseeks.domain.model.CharacterOriginLocation
import com.glootie.meeseeks.domain.model.CharacterStatus
import com.glootie.meeseeks.domain.model.CharacterSummary

@Entity(tableName = "characters")
data class CharacterEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val status: String,
    val image: String?,
    val species: String? = null,
    val gender: String? = null,
    val originLocationId: Int? = null,
    val originLocationName: String? = null,
    val lastLocationId: Int? = null,
    val lastLocationName: String? = null
)

fun CharacterEntity.toCharacterSummary(): CharacterSummary {
    return CharacterSummary(
        id = id,
        name = name,
        status = enumValueOf<CharacterStatus>(status.uppercase()),
        image = image
    )
}

fun CharacterEntity.toCharacterDetails(): CharacterDetails? {
    if (species == null || gender == null || originLocationName == null || lastLocationName == null) return null
    return CharacterDetails(
        name = name,
        status = enumValueOf<CharacterStatus>(status.uppercase()),
        species = species,
        gender = gender,
        originLocation = CharacterOriginLocation(originLocationId, originLocationName),
        lastLocation = CharacterLastLocation(lastLocationId, lastLocationName),
        image = image
    )
}
