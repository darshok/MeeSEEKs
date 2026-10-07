package com.glootie.meeseeks.data.entity.response

import com.glootie.meeseeks.data.local.entity.CharacterEntity
import com.google.gson.annotations.SerializedName

data class CharacterPaginatedResponse(
    @SerializedName("info")
    val info: PageInfoResponse,
    @SerializedName("results")
    val results: List<CharacterSummaryResponse>
)

data class PageInfoResponse(
    @SerializedName("count")
    val count: Int,
    @SerializedName("pages")
    val pages: Int,
    @SerializedName("next")
    val next: String?,
    @SerializedName("prev")
    val prev: String?
)

data class CharacterSummaryResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("image")
    val image: String?,
)

internal fun CharacterSummaryResponse.toEntity() = CharacterEntity(
    id = id,
    name = name,
    status = status,
    image = image
)