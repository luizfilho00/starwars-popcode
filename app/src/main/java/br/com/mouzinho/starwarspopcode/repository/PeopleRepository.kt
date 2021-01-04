package br.com.mouzinho.starwarspopcode.repository

import br.com.mouzinho.starwarspopcode.model.People
import io.reactivex.Observable

interface PeopleRepository {
    fun getPeople(page: Int): Observable<List<People>>
}