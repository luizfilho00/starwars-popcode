package br.com.mouzinho.starwarspopcode.ui.view.details

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.mouzinho.starwarspopcode.domain.entity.People
import br.com.mouzinho.starwarspopcode.domain.repository.PeopleRepository
import br.com.mouzinho.starwarspopcode.ui.util.DispatcherProvider
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch

class PeopleDetailViewModel @ViewModelInject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val peopleRepository: PeopleRepository
) : ViewModel() {
    val peopleDetails by lazy { _peopleDetails.asSharedFlow() }
    val favorited by lazy { _favorited.asSharedFlow().shareIn(viewModelScope, SharingStarted.Lazily) }

    private val _peopleDetails by lazy { MutableSharedFlow<People>() }
    private val _favorited by lazy { MutableSharedFlow<Boolean>() }

    fun loadDetails(people: People) {
        viewModelScope.launch(dispatcherProvider.io()) {
            val planetName = peopleRepository.loadPlanetName(people.planet)
            val speciesNames = if (people.species.isNotEmpty())
                people.species.reduce { acc, s ->
                    "${peopleRepository.loadSpecieName(acc)}, ${peopleRepository.loadSpecieName(s)}"
                }
            else people.speciesNames
            _peopleDetails.emit(people.copy(planet = planetName, speciesNames = speciesNames))
        }
    }

    fun updateFavorite(people: People) {
        val isFavorited = !people.favorite
        viewModelScope.launch(dispatcherProvider.io()) {
            peopleRepository.updatePeople(people.copy(favorite = isFavorited))
            _favorited.emit(isFavorited)
        }
    }
}