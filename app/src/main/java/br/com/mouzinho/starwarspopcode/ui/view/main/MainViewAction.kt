package br.com.mouzinho.starwarspopcode.ui.view.main

sealed class MainViewAction {
    data class ChangeToolbarTitle(val title: String) : MainViewAction()
    data class Search(val text: String) : MainViewAction()
}