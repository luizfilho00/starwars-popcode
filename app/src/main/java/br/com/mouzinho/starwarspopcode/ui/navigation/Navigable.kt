package br.com.mouzinho.starwarspopcode.ui.navigation

interface Navigable {
    suspend fun onBackPressed() {
        Navigator.popUp()
    }
}