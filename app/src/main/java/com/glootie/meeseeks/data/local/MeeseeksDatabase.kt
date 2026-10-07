package com.glootie.meeseeks.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.glootie.meeseeks.data.local.dao.CharacterDao
import com.glootie.meeseeks.data.local.dao.LocationDao
import com.glootie.meeseeks.data.local.dao.RemoteKeyDao
import com.glootie.meeseeks.data.local.entity.CharacterEntity
import com.glootie.meeseeks.data.local.entity.LocationEntity
import com.glootie.meeseeks.data.local.entity.RemoteKeyEntity

@Database(
    entities = [CharacterEntity::class, LocationEntity::class, RemoteKeyEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MeeseeksDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao
    abstract fun locationDao(): LocationDao
    abstract fun remoteKeyDao(): RemoteKeyDao
}
