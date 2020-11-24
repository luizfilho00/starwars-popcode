package br.com.mouzinho.starwarspopcode.data.network

import br.com.mouzinho.starwarspopcode.data.entity.ApiFavorite
import br.com.mouzinho.starwarspopcode.data.entity.ApiPeopleResponse
import br.com.mouzinho.starwarspopcode.data.entity.ApiPlanet
import br.com.mouzinho.starwarspopcode.data.entity.ApiSpecie
import retrofit2.http.*

interface ApiService {

    @GET("people")
    suspend fun getPeople(@Query("page") page: Int): ApiPeopleResponse

    @GET
    suspend fun getPlanetName(@Url url: String): ApiPlanet

    @GET
    suspend fun getSpecieName(@Url url: String): ApiSpecie

    @POST("http://private-782d3-starwarsfavorites.apiary-mock.com/favorite/{id}")
    suspend fun saveFavorite(@Path("id") id: String, @Header("Prefer") preferHeader: Int?): ApiFavorite
}