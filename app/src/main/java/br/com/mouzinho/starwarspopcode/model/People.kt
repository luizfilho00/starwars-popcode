package br.com.mouzinho.starwarspopcode.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class PeopleResponse(
    val count: Int?,
    val next: String?,
    val previous: String?,
    val results: List<People>?
)

data class People(
    @SerializedName("birth_year") val birthYear: String?,
    val created: Date?,
    val edited: Date?,
    @SerializedName("eye_color") val eyeColor: String?,
    val films: List<String>?,
    val gender: String?,
    @SerializedName("hair_color") val hairColor: String?,
    val height: String?,
    val homeworld: String?,
    val mass: String?,
    val name: String?,
    @SerializedName("skin_color") val skinColor: String?,
    val species: List<String>?,
    val starships: List<String>?,
    val url: String?,
    val vehicles: List<String>?
)