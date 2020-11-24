package br.com.mouzinho.starwarspopcode.ui.view.details

import br.com.mouzinho.starwarspopcode.domain.entity.People

sealed class PeopleDetailsViewAction {
    data class UpdateFavorite(val people: People) : PeopleDetailsViewAction()
    data class LoadDetails(val people: People) : PeopleDetailsViewAction()
}