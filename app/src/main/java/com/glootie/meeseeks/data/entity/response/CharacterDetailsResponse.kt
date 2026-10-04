package com.glootie.meeseeks.data.entity.response

import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import com.glootie.meeseeks.domain.model.CharacterDetails
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
    val origin: CharacterOriginResponse,
    @SerializedName("location")
    val location: CharacterLocationResponse,
    @SerializedName("image")
    val image: String?
)

data class CharacterOriginResponse(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)


data class CharacterLocationResponse(
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
    origin = origin.name,
    location = location.name,
    image = image
)
