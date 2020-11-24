package br.com.mouzinho.starwarspopcode.ui.view.people

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import br.com.mouzinho.starwarspopcode.data.dao.DbPeopleDao
import br.com.mouzinho.starwarspopcode.data.dao.DbRemoteKeyDao
import br.com.mouzinho.starwarspopcode.data.network.ApiService
import br.com.mouzinho.starwarspopcode.data.paging.PagingDataSource
import br.com.mouzinho.starwarspopcode.data.paging.PeopleBoundaryCallback
import br.com.mouzinho.starwarspopcode.domain.useCase.UpdateFavorite
import br.com.mouzinho.starwarspopcode.ui.util.DispatcherProvider
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PeopleViewModel @ViewModelInject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val pagingDataSource: PagingDataSource,
    private val peopleDao: DbPeopleDao,
    private val remoteKeyDao: DbRemoteKeyDao,
    private val apiService: ApiService,
    private val updateFavorite: UpdateFavorite
) : ViewModel() {
    val viewState by lazy { _viewState.asSharedFlow().shareIn(viewModelScope, SharingStarted.Lazily) }

    private val _viewState by lazy { MutableSharedFlow<PeopleViewState>() }
    private val initialState = PeopleViewState(
        isLoading = true,
        peopleList = null,
        peopleListUpdated = false,
        favoriteSaved = null
    )

    init {
        viewModelScope.launch {
            _viewState.emit(initialState)
            LivePagedListBuilder(
                pagingDataSource,
                PagedList.Config.Builder().setEnablePlaceholders(false).setPageSize(10).build()
            )
                .apply {
                    setBoundaryCallback(
                        PeopleBoundaryCallback(
                            viewModelScope,
                            ::updateViewState,
                            apiService,
                            peopleDao,
                            remoteKeyDao
                        )
                    )
                }
                .build()
                .asFlow().collect { pagedList ->
                    _viewState.emit(initialState.copy(peopleList = pagedList, peopleListUpdated = true))
                }
        }
    }

    fun updateViewState(action: PeopleViewAction) {
        when (action) {
            is PeopleViewAction.Search -> onSearch(action.text)
            is PeopleViewAction.UpdateFavorite -> viewModelScope.launch(dispatcherProvider.io()) {
                val favoriteSaved = updateFavorite.execute(action.people)
                _viewState.emit(initialState.copy(favoriteSaved = favoriteSaved))
            }
            is PeopleViewAction.ToggleLoadingVisiblity -> viewModelScope.launch {
                _viewState.emit(initialState.copy(isLoading = action.visible))
            }
        }
    }

    private fun onSearch(text: String) {
        pagingDataSource.query = text
        pagingDataSource.invalidate()
    }
}