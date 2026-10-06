package com.glootie.meeseeks.data.entity.response

import com.google.gson.annotations.SerializedName

data class LocationDetailsResponse(
    @SerializedName("name")
    val name: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("dimension")
    val dimension: String,
)
