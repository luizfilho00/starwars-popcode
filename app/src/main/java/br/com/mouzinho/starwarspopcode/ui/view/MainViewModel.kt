package br.com.mouzinho.starwarspopcode.ui.view

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor() : ViewModel() {
    val searchText: SharedFlow<String> by lazy { _searchText.asSharedFlow().shareIn(viewModelScope, SharingStarted.Lazily) }

    private val _searchText by lazy { MutableSharedFlow<String>() }

    fun onSearch(text: String) {
        viewModelScope.launch {
            _searchText.emit(text)
        }
    }
}