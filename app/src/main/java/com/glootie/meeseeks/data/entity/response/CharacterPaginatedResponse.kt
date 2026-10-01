package com.glootie.meeseeks.data.entity.response

import com.glootie.meeseeks.domain.model.CharacterPaginated
import com.glootie.meeseeks.domain.model.CharacterSummary
import com.glootie.meeseeks.domain.model.PageInfo
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
    val next: String,
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

internal fun CharacterPaginatedResponse.mapToDomain() = CharacterPaginated(
    info = this.info.mapToDomain(),
    results = this.results.map { it.mapToDomain() }
)

internal fun PageInfoResponse.mapToDomain() = PageInfo(
    count = this.count,
    pages = this.pages,
    next = this.next,
    prev = this.prev
)

internal fun CharacterSummaryResponse.mapToDomain() = CharacterSummary(
    id = this.id,
    name = this.name,
    status = this.status,
    image = this.image
)