package br.com.mouzinho.starwarspopcode.data.entity

import br.com.mouzinho.starwarspopcode.domain.entity.People
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class DbPeople(
    @PrimaryKey
    var id: String = UUID.randomUUID().toString(),
    var name: String = UNKNOWN,
    var height: String = UNKNOWN,
    var mass: String = UNKNOWN,
    var hairColor: String = UNKNOWN,
    var skinColor: String = UNKNOWN,
    var birthYear: String = UNKNOWN,
    var gender: String = UNKNOWN,
    var planet: String = UNKNOWN,
    var specie: String = UNKNOWN
) : RealmObject() {

    fun toPeople() = People(
        name = name,
        height = height,
        mass = mass,
        hairColor = hairColor,
        skinColor = skinColor,
        birthYear = birthYear,
        gender = gender,
        planet = planet,
        specie = specie
    )

    companion object {
        private const val UNKNOWN = "Unknown"

        fun fromPeople(people: People) = DbPeople(
            name = people.name,
            height = people.height,
            mass = people.mass,
            hairColor = people.hairColor,
            skinColor = people.skinColor,
            birthYear = people.birthYear,
            gender = people.gender,
            planet = people.planet,
            specie = people.specie
        )
    }
}