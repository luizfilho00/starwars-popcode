package br.com.mouzinho.starwarspopcode.ui.view.people

import br.com.mouzinho.starwarspopcode.domain.entity.People

sealed class PeopleViewAction {
    data class Search(val text: String) : PeopleViewAction()
    data class UpdateFavorite(val people: People) : PeopleViewAction()
    data class ToggleLoadingVisiblity(val visible: Boolean) : PeopleViewAction()
}