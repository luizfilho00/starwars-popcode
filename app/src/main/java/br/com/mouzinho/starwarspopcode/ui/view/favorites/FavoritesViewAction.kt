package br.com.mouzinho.starwarspopcode.ui.view.favorites

import br.com.mouzinho.starwarspopcode.domain.entity.People

sealed class FavoritesViewAction {
    data class Search(val text: String) : FavoritesViewAction()
    data class RemoveFavorite(val people: People) : FavoritesViewAction()
}