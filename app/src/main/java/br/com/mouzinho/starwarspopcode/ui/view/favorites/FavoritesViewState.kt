package br.com.mouzinho.starwarspopcode.ui.view.favorites

import br.com.mouzinho.starwarspopcode.domain.entity.People

data class FavoritesViewState(
    val favorites: List<People>,
    val showFavoriteRemovedMessage: Boolean,
    val favoriteRemovedMessage: String,
    val isLoading: Boolean,
    val savedAsFavorite: Boolean?,
)