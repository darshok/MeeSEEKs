package com.glootie.meeseeks.domain.model

import com.glootie.meeseeks.data.entity.response.LocationDetailsResponse

data class LocationDetails(
    val name: String,
    val type: String,
    val dimension: String,
)

fun LocationDetailsResponse.mapToDomain() = LocationDetails(
    name = name,
    type = type,
    dimension = dimension
)