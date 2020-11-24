package br.com.mouzinho.starwarspopcode.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.mouzinho.starwarspopcode.domain.entity.People
import java.util.*

@Entity
data class DbPeople(
    @PrimaryKey
    val id: String,
    val name: String,
    val height: String,
    val mass: String,
    val hairColor: String,
    val skinColor: String,
    val birthYear: String,
    val gender: String,
    val planetUrl: String,
    val speciesUrl: List<String>,
    val createdAt: Long,
    val isFavorite: Boolean
) {
    fun toPeople() = People(
        id = id,
        name = name,
        height = height,
        mass = mass,
        hairColor = hairColor,
        skinColor = skinColor,
        birthYear = birthYear,
        gender = gender,
        planetUrl = planetUrl,
        species = speciesUrl,
        favorite = isFavorite
    )

    companion object {
        fun fromPeople(people: People) = DbPeople(
            id = people.id,
            name = people.name,
            height = people.height,
            mass = people.mass,
            hairColor = people.hairColor,
            skinColor = people.skinColor,
            birthYear = people.birthYear,
            gender = people.gender,
            planetUrl = people.planetUrl,
            speciesUrl = people.species,
            createdAt = Date().time,
            isFavorite = people.favorite
        )
    }
}