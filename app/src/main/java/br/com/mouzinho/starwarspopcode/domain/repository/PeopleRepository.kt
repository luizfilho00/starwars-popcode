package br.com.mouzinho.starwarspopcode.domain.repository

import br.com.mouzinho.starwarspopcode.data.entity.ApiPeopleResponse
import io.reactivex.Single

interface PeopleRepository {
    fun getAll(page: Int): Single<ApiPeopleResponse>
}