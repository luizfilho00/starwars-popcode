package br.com.mouzinho.starwarspopcode.ui.view.favorites

import androidx.hilt.lifecycle.ViewModelInject
import br.com.mouzinho.starwarspopcode.domain.entity.People
import br.com.mouzinho.starwarspopcode.domain.repository.PeopleRepository
import br.com.mouzinho.starwarspopcode.ui.util.BaseViewModel
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable

class FavoritesViewModel @ViewModelInject constructor(
    repository: PeopleRepository
) : BaseViewModel() {
    val favoritesObservable: Observable<List<People>> by lazy { favoritesPublisher.hide() }

    private val favoritesPublisher by lazy { BehaviorRelay.create<List<People>>() }

    init {
        favoritesPublisher.accept(repository.loadAllFavorites())
    }
}