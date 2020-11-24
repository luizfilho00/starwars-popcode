package br.com.mouzinho.starwarspopcode.ui.view.details

import br.com.mouzinho.starwarspopcode.domain.entity.People

data class PeopleDetailsViewState(
    val isLoading: Boolean,
    val favorited: Boolean?,
    val peopleWithAllInformations: People?
)