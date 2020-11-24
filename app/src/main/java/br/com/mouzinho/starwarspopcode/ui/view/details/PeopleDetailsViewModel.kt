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

    fun updateViewState(action: PeopleDetailsViewAction) {
        when (action) {
            is PeopleDetailsViewAction.LoadDetails -> loadDetails(action.people)
            is PeopleDetailsViewAction.UpdateFavorite -> updateFavorite(action.people)
        }
    }

    private fun loadDetails(people: People) {
        viewModelScope.launch(dispatcherProvider.io()) {
            val speciesNames = loadSpecieNames(people)
            val planetName = peopleRepository.loadPlanetName(people.planetUrl)
            _viewState.emit(
                initialState.copy(
                    isLoading = false,
                    peopleWithAllInformations = people.copy(
                        speciesNames = speciesNames,
                        planetName = planetName
                    )
                )
            )
        }
    }

    private fun updateFavorite(people: People) {
        viewModelScope.launch {
            _viewState.emit(initialState.copy(isLoading = true))
        }
        viewModelScope.launch(dispatcherProvider.io()) {
            val favoriteSaved = updateFavorite.execute(people)
            _viewState.emit(initialState.copy(isLoading = false, favorited = favoriteSaved))
        }
    }

    private suspend fun loadSpecieNames(people: People): String {
        var speciesNames = ""
        people.species.forEach { url ->
            speciesNames += "${peopleRepository.loadSpecieName(url)}, "
        }
        return if (speciesNames.isNotEmpty()) {
            speciesNames.substring(0, speciesNames.lastIndexOf(",")).trimEnd()
        } else people.speciesNames
    }
}