package com.glootie.meeseeks.domain.model

import androidx.compose.runtime.Immutable


data class CharacterPaginated(
    val info: PageInfo,
    val results: List<CharacterSummary>
)

data class PageInfo(
    val count: Int,
    val pages: Int,
    val next: String,
    val prev: String?
)

@Immutable
data class CharacterSummary(
    val id: Int,
    val name: String,
    val status: CharacterStatus,
    val image: String?,
)