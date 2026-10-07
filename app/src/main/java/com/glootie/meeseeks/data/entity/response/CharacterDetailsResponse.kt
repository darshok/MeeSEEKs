package com.glootie.meeseeks.data.entity.response

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