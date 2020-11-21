package br.com.mouzinho.starwarspopcode.domain.entity

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
)