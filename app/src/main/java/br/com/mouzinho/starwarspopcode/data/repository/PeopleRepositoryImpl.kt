package br.com.mouzinho.starwarspopcode.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import br.com.mouzinho.starwarspopcode.data.dao.DbPeopleDao
import br.com.mouzinho.starwarspopcode.data.entity.DbPeople
import br.com.mouzinho.starwarspopcode.data.network.ApiService
import br.com.mouzinho.starwarspopcode.domain.entity.People
import br.com.mouzinho.starwarspopcode.domain.repository.PeopleRepository
import javax.inject.Inject

class PeopleRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val peopleDao: DbPeopleDao,
) : PeopleRepository {

    override suspend fun loadPlanetName(url: String): String {
        return apiService.getPlanetName(url).name ?: ""
    }

    override suspend fun loadSpecieName(url: String): String {
        return ""
    }

    override suspend fun updatePeople(people: People) {
        peopleDao.updatePeople(DbPeople.fromPeople(people))
    }

    override fun loadAllFavorites(): LiveData<List<People>> {
        return Transformations.map(peopleDao.getAllFavorites()) { list ->
            list.map { it.toPeople() }
        }
    }
}