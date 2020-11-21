package br.com.mouzinho.starwarspopcode.data.network

import br.com.mouzinho.starwarspopcode.data.entity.ApiPeopleResponse
import br.com.mouzinho.starwarspopcode.data.entity.ApiPlanet
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiService {

    @GET("people")
    fun getPeople(@Query("page") page: Int): Single<ApiPeopleResponse>

    @GET
    fun getPlanetName(@Url url: String): Single<ApiPlanet>
}