package br.com.mouzinho.starwarspopcode.data.entity

import com.google.gson.annotations.SerializedName

data class ApiError(
    val error: String,
    @SerializedName("error_message") val errorMessage: String
)