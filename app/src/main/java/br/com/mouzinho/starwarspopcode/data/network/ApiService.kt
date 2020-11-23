package br.com.mouzinho.starwarspopcode.data.network

import br.com.mouzinho.starwarspopcode.data.entity.ApiPeopleResponse
import br.com.mouzinho.starwarspopcode.data.entity.ApiPlanet
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiService {

    @GET("people")
    suspend fun getPeople(@Query("page") page: Int): ApiPeopleResponse

    @GET
    suspend fun getPlanetName(@Url url: String): ApiPlanet
}