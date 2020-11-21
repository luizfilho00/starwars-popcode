package br.com.mouzinho.starwarspopcode.domain.repository

import br.com.mouzinho.starwarspopcode.domain.entity.People
import io.reactivex.Single

interface PeopleRepository {
    fun savePeopleListIntoDb(list: List<People>)
    fun saveAsFavorite(people: People)
    fun removeFromFavorites(people: People)
    fun loadAllFromDb(): List<People>
    fun loadAllFavorites(): List<People>
    fun loadPlanetName(url: String): Single<String>
    fun loadSpecieName(url: String): Single<String>
}