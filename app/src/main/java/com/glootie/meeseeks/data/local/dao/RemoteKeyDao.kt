package com.glootie.meeseeks.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.glootie.meeseeks.data.local.entity.RemoteKeyEntity

@Dao
interface RemoteKeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKeys: List<RemoteKeyEntity>): List<Long>

    @Query("SELECT * FROM remote_keys WHERE id = :id")
    suspend fun remoteKeyId(id: Int): RemoteKeyEntity?

    @Query("DELETE FROM remote_keys")
    suspend fun clearRemoteKeys(): Int
}
