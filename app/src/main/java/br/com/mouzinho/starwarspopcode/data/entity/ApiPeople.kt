package br.com.mouzinho.starwarspopcode.data.entity

import br.com.mouzinho.starwarspopcode.domain.entity.People
import com.google.gson.annotations.SerializedName
import java.util.*

data class ApiPeopleResponse(
    val count: Int?,
    val next: String?,
    val previous: String?,
    val results: List<ApiPeople>?
)

data class ApiPeople(
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
) {

    fun toPeople() = People(
        name = name ?: throw NullPointerException("Nome não pode ser null"),
        height = if (height.isNullOrEmpty()) "" else height,
        mass = if (mass.isNullOrEmpty()) UNKNOWN else mass,
        hairColor = if (hairColor.isNullOrEmpty()) UNKNOWN else hairColor,
        skinColor = if (skinColor.isNullOrEmpty()) UNKNOWN else skinColor,
        birthYear = if (birthYear.isNullOrEmpty()) UNKNOWN else birthYear,
        gender = if (gender.isNullOrEmpty()) UNKNOWN else gender,
        planet = UNKNOWN, //TODO -> Chamada api para pegar
        specie = UNKNOWN //TODO -> Chamada api para pegar
    )

    companion object {
        private const val UNKNOWN = "Unknown"
    }
}