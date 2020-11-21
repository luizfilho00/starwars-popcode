package br.com.mouzinho.starwarspopcode.data.paging

import androidx.paging.rxjava2.RxPagingSource
import br.com.mouzinho.starwarspopcode.data.network.ApiService
import br.com.mouzinho.starwarspopcode.domain.entity.People
import br.com.mouzinho.starwarspopcode.domain.repository.PeopleRepository
import br.com.mouzinho.starwarspopcode.ui.util.Constants
import br.com.mouzinho.starwarspopcode.ui.util.SchedulerProvider
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Single
import javax.inject.Inject

class PeopleDataSource @Inject constructor(
    private val apiService: ApiService,
    private val repository: PeopleRepository,
    private val schedulerProvider: SchedulerProvider
) : RxPagingSource<Int, People>() {
    val initialLoadingObservable = PublishRelay.create<Boolean>()

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, People>> {
        return apiService.getPeople(params.key ?: 1)
            .observeOn(schedulerProvider.ui())
            .doOnSuccess { response ->
                val peopleList = response.results?.map { it.toPeople() } ?: emptyList()
                repository.savePeopleListIntoDb(peopleList)
            }
            .map<LoadResult<Int, People>> { response ->
                initialLoadingObservable.accept(params.key == 1 || params.key == null)
                LoadResult.Page(
                    response.results?.map { it.toPeople() } ?: emptyList(),
                    response.previous?.substringAfter("=")?.toIntOrNull(),
                    response.next?.substringAfter("=")?.toIntOrNull()
                )
            }
            .onErrorResumeNext {
                Single.just(
                    LoadResult.Page(
                        repository.loadAllFromDb(),
                        null,
                        null
                    )
                )
            }
            .onErrorReturnItem(LoadResult.Error(Throwable((Constants.defaultErrorMessage))))
    }
}