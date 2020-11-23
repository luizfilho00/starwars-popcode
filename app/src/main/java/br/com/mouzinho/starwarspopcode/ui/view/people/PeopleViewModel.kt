package br.com.mouzinho.starwarspopcode.ui.view.people

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import br.com.mouzinho.starwarspopcode.data.dao.DbPeopleDao
import br.com.mouzinho.starwarspopcode.data.dao.DbRemoteKeyDao
import br.com.mouzinho.starwarspopcode.data.network.ApiService
import br.com.mouzinho.starwarspopcode.data.paging.PagingDataSource
import br.com.mouzinho.starwarspopcode.data.paging.PeopleBoundaryCallback
import br.com.mouzinho.starwarspopcode.domain.entity.People
import br.com.mouzinho.starwarspopcode.domain.repository.PeopleRepository
import br.com.mouzinho.starwarspopcode.ui.util.DispatcherProvider
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class PeopleViewModel @ViewModelInject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val pagingDataSource: PagingDataSource,
    private val peopleDao: DbPeopleDao,
    private val remoteKeyDao: DbRemoteKeyDao,
    private val apiService: ApiService,
    private val repository: PeopleRepository
) : ViewModel() {
    val favoriteObservable: SharedFlow<Boolean> by lazy { favoritePublisher.asSharedFlow() }
    val loadingObservable: SharedFlow<Boolean> by lazy { loadingPublisher.asSharedFlow() }

    private val favoritePublisher by lazy { MutableSharedFlow<Boolean>() }
    private val loadingPublisher by lazy { MutableSharedFlow<Boolean>() }

    val peopleLiveData: LiveData<PagedList<People>> =
        LivePagedListBuilder(
            pagingDataSource,
            PagedList.Config.Builder().setEnablePlaceholders(false).setPageSize(10).build()
        ).apply {
            setBoundaryCallback(PeopleBoundaryCallback(viewModelScope, loadingPublisher, apiService, peopleDao, remoteKeyDao))
        }.build()

    fun updateFavorite(people: People) =
        viewModelScope.launch(dispatcherProvider.io()) {
            val isFavorite = !people.favorite
            repository.updatePeople(people.copy(favorite = isFavorite))
            favoritePublisher.emit(isFavorite)
        }

    fun onSearch(text: String) {
        pagingDataSource.query = text
        pagingDataSource.invalidate()
    }
}