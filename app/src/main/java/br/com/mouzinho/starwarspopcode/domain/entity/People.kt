package br.com.mouzinho.starwarspopcode.domain.entity

import br.com.mouzinho.starwarspopcode.data.entity.ApiPeople
import java.lang.NullPointerException
import java.util.*

data class People(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val height: String,
    val mass: String,
    val hairColor: String,
    val skinColor: String,
    val birthYear: String,
    val gender: String,
    val planet: String,
    val specie: String
) {

    companion object {
        private const val UNKNOWN = "Unknown"

        fun fromApiPeople(apiPeople: ApiPeople) = People(
            name = apiPeople.name ?: throw NullPointerException("Nome não pode ser null"),
            height = apiPeople.height ?: "",
            mass = apiPeople.mass ?: UNKNOWN,
            hairColor = apiPeople.hairColor ?: UNKNOWN,
            skinColor = apiPeople.skinColor ?: UNKNOWN,
            birthYear = apiPeople.birthYear ?: UNKNOWN,
            gender = apiPeople.gender ?: UNKNOWN,
            planet = UNKNOWN, //TODO -> Chamada api para pegar
            specie = UNKNOWN //TODO -> Chamada api para pegar
        )
    }
}