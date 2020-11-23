package br.com.mouzinho.starwarspopcode.domain.entity

import java.io.Serializable

data class People(
    val id: String,
    val name: String,
    val height: String,
    val mass: String,
    val hairColor: String,
    val skinColor: String,
    val birthYear: String,
    val gender: String,
    val planet: String,
    val species: List<String>,
    val speciesNames: String = UNKOWN,
    var favorite: Boolean = false
) : Serializable {

    companion object {
        private const val UNKOWN = "Unknown"
    }
}