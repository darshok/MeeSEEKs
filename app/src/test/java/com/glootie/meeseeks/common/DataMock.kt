package com.glootie.meeseeks.common

import com.glootie.meeseeks.data.entity.response.CharacterDetailsResponse
import com.glootie.meeseeks.data.entity.response.CharacterLastLocationResponse
import com.glootie.meeseeks.data.entity.response.CharacterOriginLocationResponse
import com.glootie.meeseeks.data.entity.response.CharacterPaginatedResponse
import com.glootie.meeseeks.data.entity.response.CharacterSummaryResponse
import com.glootie.meeseeks.data.entity.response.LocationDetailsResponse
import com.glootie.meeseeks.data.entity.response.PageInfoResponse
import com.glootie.meeseeks.domain.model.CharacterDetails
import com.glootie.meeseeks.domain.model.CharacterLastLocation
import com.glootie.meeseeks.domain.model.CharacterOriginLocation
import com.glootie.meeseeks.domain.model.CharacterStatus
import com.glootie.meeseeks.domain.model.CharacterSummary
import com.glootie.meeseeks.domain.model.LocationDetails

internal object DataMock {

    val sampleCharacterSummaryResponse = CharacterSummaryResponse(
        id = 1,
        name = "Rick Sanchez",
        status = "Alive",
        image = "https://example.com/rick.png"
    )

    val sampleCharacterSummary = CharacterSummary(
        id = 1,
        name = "Rick Sanchez",
        status = CharacterStatus.ALIVE,
        image = "https://example.com/rick.png"
    )

    val samplePageInfoResponse = PageInfoResponse(
        count = 1,
        pages = 1,
        next = "https://rickandmortyapi.com/api/character/?page=2",
        prev = null
    )

    val samplePaginatedResponse = CharacterPaginatedResponse(
        info = samplePageInfoResponse,
        results = listOf(sampleCharacterSummaryResponse)
    )

    val sampleCharacterDetailsResponse = CharacterDetailsResponse(
        name = "Summer Smith",
        status = "Alive",
        species = "Human",
        gender = "Female",
        originLocation = CharacterOriginLocationResponse("Earth", "https://example.com/earth"),
        lastLocation = CharacterLastLocationResponse("Earth", "https://example.com/earth"),
        image = "https://example.com/summer.png"
    )

    val sampleCharacterDetails = CharacterDetails(
        name = "Summer Smith",
        status = CharacterStatus.ALIVE,
        species = "Human",
        gender = "Female",
        originLocation = CharacterOriginLocation(name = "Earth", id = 1),
        lastLocation = CharacterLastLocation(name = "Earth", id = 1),
        image = "https://example.com/summer.png"
    )

    val sampleLocationDetailsResponse = LocationDetailsResponse(
        name = "Earth (C-137)",
        type = "Planet",
        dimension = "Dimension C-137"
    )

    val sampleLocationDetails = LocationDetails(
        name = "Earth (C-137)",
        type = "Planet",
        dimension = "Dimension C-137"
    )
}
