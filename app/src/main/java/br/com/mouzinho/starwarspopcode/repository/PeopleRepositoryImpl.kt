package br.com.mouzinho.starwarspopcode.repository

import br.com.mouzinho.starwarspopcode.model.People
import br.com.mouzinho.starwarspopcode.network.ApiService
import io.reactivex.Observable
import javax.inject.Inject

class PeopleRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : PeopleRepository {

    override fun getPeople(page: Int): Observable<List<People>> {
        return apiService.getPeople(page)
            .map { response -> response.results }
    }
}