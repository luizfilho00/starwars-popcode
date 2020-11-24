package br.com.mouzinho.starwarspopcode.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import br.com.mouzinho.starwarspopcode.data.dao.DbPeopleDao
import br.com.mouzinho.starwarspopcode.data.entity.DbPeople
import br.com.mouzinho.starwarspopcode.data.network.ApiErrorHandler
import br.com.mouzinho.starwarspopcode.data.network.ApiService
import br.com.mouzinho.starwarspopcode.domain.entity.People
import br.com.mouzinho.starwarspopcode.domain.entity.SaveFavoriteResponse
import br.com.mouzinho.starwarspopcode.domain.repository.PeopleRepository
import br.com.mouzinho.starwarspopcode.domain.util.StringResource
import javax.inject.Inject

class PeopleRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val peopleDao: DbPeopleDao,
    private val stringResource: StringResource,
    private val errorHandler: ApiErrorHandler
) : PeopleRepository {

    override suspend fun loadPlanetName(url: String): String {
        return apiService.getPlanetName(url).name ?: ""
    }

    override suspend fun loadSpecieName(url: String): String {
        return apiService.getSpecieName(url).name ?: ""
    }

    override suspend fun updatePeopleAsFavoriteOnNetwork(people: People, preferHeader: Int?): SaveFavoriteResponse {
        return try {
            val response = apiService.saveFavorite(people.id, preferHeader)
            peopleDao.updatePeople(DbPeople.fromPeople(people))
            SaveFavoriteResponse(people.favorite, response.message)
        } catch (ex: Exception) {
            val apiError = errorHandler.getApiError(ex)
            SaveFavoriteResponse(people.favorite, apiError.errorMessage)
        }
    }

    override suspend fun updatePeopleAsFavoriteLocally(people: People): SaveFavoriteResponse {
        peopleDao.updatePeople(DbPeople.fromPeople(people))
        return if (!people.favorite) SaveFavoriteResponse(people.favorite, stringResource.removedFavoriteMessage)
        else SaveFavoriteResponse(people.favorite, stringResource.savedFavoriteMessage)
    }

    override suspend fun getPeopleById(id: String): People? {
        return peopleDao.getPeopleById(id).firstOrNull()?.toPeople()
    }

    override fun loadAllFavorites(): LiveData<List<People>> {
        return Transformations.map(peopleDao.getAllFavorites()) { list ->
            list.map { it.toPeople() }
        }
    }
}