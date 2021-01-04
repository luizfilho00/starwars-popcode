package br.com.mouzinho.starwarspopcode.presentation.view.main

import br.com.mouzinho.starwarspopcode.model.People

sealed class MainViewState {
    data class ShowPeople(val list: List<People>) : MainViewState()
    data class ShowError(val message: String?) : MainViewState()
    object ShowLoading : MainViewState()
    object HideLoading : MainViewState()
}