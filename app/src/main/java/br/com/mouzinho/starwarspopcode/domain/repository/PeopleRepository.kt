package br.com.mouzinho.starwarspopcode.domain.repository

import androidx.lifecycle.LiveData
import br.com.mouzinho.starwarspopcode.domain.entity.People
import br.com.mouzinho.starwarspopcode.domain.entity.SaveFavoriteResponse

interface PeopleRepository {
    suspend fun updatePeopleAsFavoriteLocally(people: People): SaveFavoriteResponse
    suspend fun updatePeopleAsFavoriteOnNetwork(people: People, preferHeader: Int?): SaveFavoriteResponse
    suspend fun getPeopleById(id: String): People?
    fun loadAllFavorites(): LiveData<List<People>>
    suspend fun loadPlanetName(url: String): String
    suspend fun loadSpecieName(url: String): String
}