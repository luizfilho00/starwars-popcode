package br.com.mouzinho.starwarspopcode.data.paging

import androidx.paging.PagedList
import br.com.mouzinho.starwarspopcode.data.dao.DbPeopleDao
import br.com.mouzinho.starwarspopcode.data.dao.DbRemoteKeyDao
import br.com.mouzinho.starwarspopcode.data.entity.DbPeople
import br.com.mouzinho.starwarspopcode.data.entity.DbRemoteKey
import br.com.mouzinho.starwarspopcode.data.network.ApiService
import br.com.mouzinho.starwarspopcode.domain.entity.People
import br.com.mouzinho.starwarspopcode.ui.view.people.PeoplePagingController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

class PeopleBoundaryCallback(
    private val scope: CoroutineScope,
    private val loadingObservable: MutableSharedFlow<Boolean>,
    private val apiService: ApiService,
    private val peopleDao: DbPeopleDao,
    private val remoteKeyDao: DbRemoteKeyDao
) : PagedList.BoundaryCallback<People>() {
    private val loading = AtomicBoolean(false)

    override fun onZeroItemsLoaded() {
        if (loading.get()) return
        scope.launch {
            loading.set(true)
            loadingObservable.emit(true)
            var currentPage = 1
            val response = try {
                apiService.getPeople(currentPage)
            } catch (ex: Exception) {
                loadingObservable.emit(false)
                loading.set(false)
                null
            }
            if (response?.results != null) {
                loadingObservable.emit(false)
                remoteKeyDao.insertRemoteKey(DbRemoteKey(0, ++currentPage))
                peopleDao.insertAllPeople(response.results.map { DbPeople.fromPeople(it.toPeople()) })
                loading.set(false)
            }
        }
    }

    override fun onItemAtEndLoaded(itemAtEnd: People) {
        if (loading.get()) return
        scope.launch {
            loading.set(true)
            loadingObservable.emit(true)
            var currentPage = remoteKeyDao.getAllRemoteKey().firstOrNull()?.nextKey
            if (currentPage != null) {
                val response = try {
                    apiService.getPeople(currentPage)
                } catch (ex: Exception) {
                    loadingObservable.emit(false)
                    loading.set(false)
                    null
                }
                if (response?.results != null) {
                    loadingObservable.emit(false)
                    remoteKeyDao.insertRemoteKey(DbRemoteKey(0, ++currentPage))
                    peopleDao.insertAllPeople(response.results.map { DbPeople.fromPeople(it.toPeople()) })
                    loading.set(false)
                }
            }
        }
    }
}