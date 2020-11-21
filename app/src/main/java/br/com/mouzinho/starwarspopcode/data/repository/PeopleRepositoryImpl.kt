package br.com.mouzinho.starwarspopcode.data.repository

import br.com.mouzinho.starwarspopcode.data.entity.ApiPeopleResponse
import br.com.mouzinho.starwarspopcode.data.network.ApiService
import br.com.mouzinho.starwarspopcode.domain.repository.PeopleRepository
import io.reactivex.Single
import javax.inject.Inject

class PeopleRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : PeopleRepository {

    override fun getAll(page: Int): Single<ApiPeopleResponse> {
        return apiService.getPeople(page)
    }
}