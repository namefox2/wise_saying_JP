package com.kotoba.takarabako.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey val id: String,
    val type: String
)

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorites WHERE type = :type")
    fun getAll(type: String): Flow<List<FavoriteEntity>>

    @Query("SELECT id FROM favorites WHERE type = :type")
    fun getAllIds(type: String): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(fav: FavoriteEntity)

    @Delete
    suspend fun delete(fav: FavoriteEntity)
}
