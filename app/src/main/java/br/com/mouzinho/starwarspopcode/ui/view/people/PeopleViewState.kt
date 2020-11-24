package br.com.mouzinho.starwarspopcode.ui.view.people

import androidx.paging.PagedList
import br.com.mouzinho.starwarspopcode.domain.entity.People

data class PeopleViewState(
    val isLoading: Boolean,
    val peopleListUpdated: Boolean,
    val peopleList: PagedList<People>?,
    val favoriteSaved: Boolean?
)