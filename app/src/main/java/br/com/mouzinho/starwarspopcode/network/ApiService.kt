package br.com.mouzinho.starwarspopcode.network

import br.com.mouzinho.starwarspopcode.model.PeopleResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("people")
    fun getPeople(@Query("page") page: Int): Observable<PeopleResponse>
}