package br.com.mouzinho.starwarspopcode.ui.view.details

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.mouzinho.starwarspopcode.domain.entity.People
import br.com.mouzinho.starwarspopcode.domain.repository.PeopleRepository
import br.com.mouzinho.starwarspopcode.domain.useCase.UpdateFavorite
import br.com.mouzinho.starwarspopcode.ui.util.DispatcherProvider
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch

class PeopleDetailsViewModel @ViewModelInject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val peopleRepository: PeopleRepository,
    private val updateFavorite: UpdateFavorite
) : ViewModel() {
    val viewState by lazy { _viewState.asSharedFlow().shareIn(viewModelScope, SharingStarted.Lazily) }

    private val _viewState by lazy { MutableSharedFlow<PeopleDetailsViewState>() }
    private val initialState = PeopleDetailsViewState(
        isLoading = true,
        favorited = null,
        peopleWithAllInformations = null
    )

    init {
        viewModelScope.launch {
            _viewState.emit(initialState)
        }
    }

    fun updateViewState(action: PeopleDetailsViewAction) {
        when (action) {
            is PeopleDetailsViewAction.LoadDetails -> viewModelScope.launch(dispatcherProvider.io()) {
                val speciesNames = loadSpecieNames(action.people)
                _viewState.emit(
                    initialState.copy(
                        isLoading = false,
                        peopleWithAllInformations = action.people.copy(
                            speciesNames = speciesNames,
                            planetName = peopleRepository.loadPlanetName(action.people.planetUrl)
                        )
                    )
                )
            }
            is PeopleDetailsViewAction.UpdateFavorite -> {
                viewModelScope.launch {
                    _viewState.emit(initialState.copy(isLoading = true))
                }
                viewModelScope.launch(dispatcherProvider.io()) {
                    val favoriteSaved = updateFavorite.execute(action.people)
                    _viewState.emit(initialState.copy(isLoading = false, favorited = favoriteSaved))
                }
            }
        }
    }

    private suspend fun loadSpecieNames(people: People): String {
        return if (people.species.isNotEmpty())
            people.species.reduce { acc, s ->
                "${peopleRepository.loadSpecieName(acc)}, ${peopleRepository.loadSpecieName(s)}"
            }
        else people.speciesNames
    }
}