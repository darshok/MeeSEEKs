package com.glootie.meeseeks.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.glootie.meeseeks.data.local.entity.LocationEntity

@Dao
interface LocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(location: LocationEntity): Long

    @Query("SELECT * FROM locations WHERE id = :id")
    suspend fun getLocationById(id: Int): LocationEntity?
}
