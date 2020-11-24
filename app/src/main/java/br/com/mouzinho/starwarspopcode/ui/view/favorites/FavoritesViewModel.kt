package br.com.mouzinho.starwarspopcode.ui.view.favorites

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import br.com.mouzinho.starwarspopcode.domain.repository.PeopleRepository
import br.com.mouzinho.starwarspopcode.domain.useCase.UpdateFavorite
import br.com.mouzinho.starwarspopcode.ui.util.DispatcherProvider
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FavoritesViewModel @ViewModelInject constructor(
    private val repository: PeopleRepository,
    private val dispatcherProvider: DispatcherProvider,
    private val updateFavorite: UpdateFavorite
) : ViewModel() {
    val viewState by lazy { _viewState.asSharedFlow().shareIn(viewModelScope, SharingStarted.Lazily) }

    private val _viewState by lazy { MutableSharedFlow<FavoritesViewState>() }
    private val favoritesFlow = repository.loadAllFavorites().asFlow()
    private val initialState = FavoritesViewState(
        favorites = emptyList(),
        isLoading = true
    )

    init {
        emitFavorites()
    }

    fun updateViewState(action: FavoritesViewAction) {
        reduceState(action)
    }

    private fun reduceState(action: FavoritesViewAction) {
        when (action) {
            is FavoritesViewAction.RemoveFavorite -> {
                viewModelScope.launch(dispatcherProvider.io()) {
                    updateFavorite.execute(action.people)
                    emitFavorites()
                }
            }
            is FavoritesViewAction.Search -> viewModelScope.launch(dispatcherProvider.io()) {
                favoritesFlow.collectLatest { favorites ->
                    _viewState.emit(
                        initialState.copy(
                            favorites = favorites.filter { it.name.contains(action.text, true) },
                            isLoading = false
                        )
                    )
                }
            }
        }
    }

    private fun emitFavorites() {
        viewModelScope.launch(dispatcherProvider.io()) {
            favoritesFlow.collectLatest { favorites ->
                _viewState.emit(initialState.copy(favorites = favorites, isLoading = false))
            }
        }
    }
}