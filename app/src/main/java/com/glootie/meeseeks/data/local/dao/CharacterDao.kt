package com.glootie.meeseeks.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.glootie.meeseeks.data.local.entity.CharacterEntity

@Dao
interface CharacterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(characters: List<CharacterEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(character: CharacterEntity): Long

    @Query("SELECT * FROM characters WHERE name LIKE '%' || :query || '%' ORDER BY id ASC")
    fun pagingSource(query: String): PagingSource<Int, CharacterEntity>

    @Query("SELECT * FROM characters WHERE id = :id")
    suspend fun getCharacterById(id: Int): CharacterEntity?

    @Query("SELECT COUNT(*) FROM characters")
    suspend fun getCharacterCount(): Int

    @Query("DELETE FROM characters")
    suspend fun clearAll(): Int
}
