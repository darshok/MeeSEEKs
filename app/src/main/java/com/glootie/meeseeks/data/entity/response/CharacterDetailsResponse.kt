package com.glootie.meeseeks.data.entity.response

import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import com.glootie.meeseeks.domain.model.CharacterDetails
import com.glootie.meeseeks.domain.model.CharacterLastLocation
import com.glootie.meeseeks.domain.model.CharacterOriginLocation
import com.glootie.meeseeks.domain.model.CharacterStatus
import com.google.gson.annotations.SerializedName

data class CharacterDetailsResponse(
    @SerializedName("name")
    val name: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("species")
    val species: String,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("origin")
    val originLocation: CharacterOriginLocationResponse,
    @SerializedName("location")
    val lastLocation: CharacterLastLocationResponse,
    @SerializedName("image")
    val image: String?
)

data class CharacterOriginLocationResponse(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)


data class CharacterLastLocationResponse(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)

fun CharacterDetailsResponse.mapToDomain() = CharacterDetails(
    name = name,
    status = enumValueOf<CharacterStatus>(status.toUpperCase(Locale.current)),
    species = species,
    gender = gender,
    originLocation = originLocation.mapToDomain(),
    lastLocation = lastLocation.mapToDomain(),
    image = image
)

fun CharacterOriginLocationResponse.mapToDomain() = CharacterOriginLocation(
    id = url.substringAfterLast("/").toIntOrNull(),
    name = name
)

fun CharacterLastLocationResponse.mapToDomain() = CharacterLastLocation(
    id = url.substringAfterLast("/").toIntOrNull(),
    name = name
)