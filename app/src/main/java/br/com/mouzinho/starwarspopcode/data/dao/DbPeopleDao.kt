package br.com.mouzinho.starwarspopcode.data.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import br.com.mouzinho.starwarspopcode.data.entity.DbPeople
import br.com.mouzinho.starwarspopcode.domain.entity.People

@Dao
interface DbPeopleDao {
    @Query("SELECT DISTINCT * FROM DbPeople")
    fun getAllPeople(): DataSource.Factory<Int, DbPeople>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPeople(list: List<DbPeople>)

    @Query(
        """
        SELECT DISTINCT * FROM dbpeople as people
        WHERE isFavorite = 1
    """
    )
    fun getAllFavorites(): LiveData<List<DbPeople>>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePeople(people: DbPeople)

    @Query("SELECT * FROM dbpeople WHERE id = :id")
    suspend fun getPeopleById(id: String): List<DbPeople>

    @Query("SELECT * FROM DbPeople WHERE name LIKE :name ORDER BY createdAt")
    fun searchPeopleByName(name: String): DataSource.Factory<Int, DbPeople>
}