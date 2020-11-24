package br.com.mouzinho.starwarspopcode.domain.entity

data class SaveFavoriteResponse(
    val savedAsFavorite: Boolean,
    val message: String
)