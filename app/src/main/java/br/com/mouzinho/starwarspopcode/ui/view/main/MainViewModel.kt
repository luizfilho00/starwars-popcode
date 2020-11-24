package br.com.mouzinho.starwarspopcode.ui.view.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.mouzinho.starwarspopcode.domain.util.StringResource
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(
    stringResource: StringResource
) : ViewModel() {
    val mainViewState: SharedFlow<MainViewState> by lazy {
        _mainViewState.asSharedFlow().shareIn(viewModelScope, SharingStarted.Lazily)
    }

    private val _mainViewState by lazy { MutableSharedFlow<MainViewState>() }
    private var initialState =
        MainViewState(
            toolbarTitle = stringResource.toolbarTitle,
            searchText = ""
        )

    init {
        viewModelScope.launch {
            _mainViewState.emit(initialState)
        }
    }

    fun updateViewState(action: MainViewAction) {
        viewModelScope.launch {
            _mainViewState.emit(reduceState(action))
        }
    }

    private fun reduceState(action: MainViewAction): MainViewState {
        return when (action) {
            is MainViewAction.ChangeToolbarTitle -> initialState.copy(toolbarTitle = action.title)
            is MainViewAction.Search -> initialState.copy(searchText = action.text)
        }
    }
}