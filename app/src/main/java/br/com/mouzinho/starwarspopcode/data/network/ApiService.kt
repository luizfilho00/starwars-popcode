package br.com.mouzinho.starwarspopcode.data.network

import br.com.mouzinho.starwarspopcode.data.entity.ApiPeopleResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("people")
    fun getPeople(@Query("page") page: Int): Single<ApiPeopleResponse>
}