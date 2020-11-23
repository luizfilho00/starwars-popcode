package br.com.mouzinho.starwarspopcode.ui.util

import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class DefaultDispatcherProvider @Inject constructor() : DispatcherProvider {
    override fun io() = Dispatchers.IO

    override fun ui() = Dispatchers.Main

    override fun computation() = Dispatchers.IO
}