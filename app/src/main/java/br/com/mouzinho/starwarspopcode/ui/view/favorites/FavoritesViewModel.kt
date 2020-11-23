package br.com.mouzinho.starwarspopcode.ui.view.favorites

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import br.com.mouzinho.starwarspopcode.domain.entity.People
import br.com.mouzinho.starwarspopcode.domain.repository.PeopleRepository
import br.com.mouzinho.starwarspopcode.ui.util.DispatcherProvider
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FavoritesViewModel @ViewModelInject constructor(
    private val repository: PeopleRepository,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {
    val favorites by lazy { _favorites.asSharedFlow() }

    private val _favorites by lazy { MutableSharedFlow<List<People>>() }
    private val favoritesLiveData by lazy { repository.loadAllFavorites() }

    init {
        reloadFavorites()
    }

    suspend fun search(text: String) =
        viewModelScope.launch {
            favoritesLiveData
                .asFlow()
                .collectLatest {
                    _favorites.emit(it.filter { it.name.contains(text, true) })
                }
        }

    fun onRemoveFavorite(people: People) {
        viewModelScope.launch(dispatcherProvider.io()) {
            repository.updatePeople(people.copy(favorite = false))
            reloadFavorites()
        }
    }

    private fun reloadFavorites() {
        viewModelScope.launch {
            favoritesLiveData
                .asFlow()
                .collectLatest {
                    _favorites.emit(it)
                }
        }
    }
}