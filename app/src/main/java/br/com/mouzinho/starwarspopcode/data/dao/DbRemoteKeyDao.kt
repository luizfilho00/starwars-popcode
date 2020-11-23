package br.com.mouzinho.starwarspopcode.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.mouzinho.starwarspopcode.data.entity.DbRemoteKey

@Dao
interface DbRemoteKeyDao {
    @Query("SELECT * FROM DbRemoteKey")
    suspend fun getAllRemoteKey(): List<DbRemoteKey>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRemoteKey(dbRemoteKey: DbRemoteKey)
}