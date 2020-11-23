package br.com.mouzinho.starwarspopcode.ui.util

fun consume(callback: () -> Unit): Boolean {
    callback()
    return true
}