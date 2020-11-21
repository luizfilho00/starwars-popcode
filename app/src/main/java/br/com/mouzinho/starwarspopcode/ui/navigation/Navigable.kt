package br.com.mouzinho.starwarspopcode.ui.navigation

interface Navigable {
    fun onBackPressed() {
        Navigator.popUp()
    }
}