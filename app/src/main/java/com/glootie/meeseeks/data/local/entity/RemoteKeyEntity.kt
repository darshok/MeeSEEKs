package com.glootie.meeseeks.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeyEntity(
    @PrimaryKey val id: Int,
    val prevKey: Int?,
    val nextKey: Int?
)
