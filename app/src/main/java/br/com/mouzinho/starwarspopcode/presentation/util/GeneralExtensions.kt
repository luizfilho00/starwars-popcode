package br.com.mouzinho.starwarspopcode.presentation.util

fun consume(callback: () -> Unit): Boolean {
    callback()
    return true
}