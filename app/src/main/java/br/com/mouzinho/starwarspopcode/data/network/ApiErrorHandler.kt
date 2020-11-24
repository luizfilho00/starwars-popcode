package br.com.mouzinho.starwarspopcode.data.network

import br.com.mouzinho.starwarspopcode.data.entity.ApiError
import br.com.mouzinho.starwarspopcode.domain.util.StringResource
import com.google.gson.Gson
import retrofit2.HttpException
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiErrorHandler @Inject constructor(
    private val stringResource: StringResource
) {

    fun getApiError(ex: Exception): ApiError {
        return if (ex is HttpException) {
            if (ex.code() == 400) {
                ex.response()?.errorBody()?.byteStream()?.reader().use { reader ->
                    Gson().fromJson(reader, ApiError::class.java)
                }
            } else {
                ApiError(stringResource.defaultErrorMessage, stringResource.defaultErrorMessage)
            }
        } else {
            ApiError(stringResource.defaultErrorMessage, stringResource.defaultErrorMessage)
        }
    }
}