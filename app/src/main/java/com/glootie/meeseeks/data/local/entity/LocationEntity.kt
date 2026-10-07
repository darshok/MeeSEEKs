package com.glootie.meeseeks.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.glootie.meeseeks.domain.model.LocationDetails

@Entity(tableName = "locations")
data class LocationEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val type: String,
    val dimension: String
)

fun LocationEntity.toLocationDetails(): LocationDetails = LocationDetails(
    name = name,
    type = type,
    dimension = dimension
)
