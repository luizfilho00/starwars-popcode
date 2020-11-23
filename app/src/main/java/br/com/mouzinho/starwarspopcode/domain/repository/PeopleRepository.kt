package br.com.mouzinho.starwarspopcode.domain.repository

import androidx.lifecycle.LiveData
import br.com.mouzinho.starwarspopcode.domain.entity.People

interface PeopleRepository {
    suspend fun updatePeople(people: People)
    fun loadAllFavorites(): LiveData<List<People>>
    suspend fun loadPlanetName(url: String): String
    suspend fun loadSpecieName(url: String): String
}