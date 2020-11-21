package br.com.mouzinho.starwarspopcode.data.paging

import androidx.paging.rxjava2.RxPagingSource
import br.com.mouzinho.starwarspopcode.data.network.ApiService
import br.com.mouzinho.starwarspopcode.domain.entity.People
import br.com.mouzinho.starwarspopcode.domain.repository.PeopleRepository
import br.com.mouzinho.starwarspopcode.ui.util.Constants
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Single
import javax.inject.Inject

class PeopleDataSource @Inject constructor(
    private val apiService: ApiService,
    private val repository: PeopleRepository
) : RxPagingSource<Int, People>() {
    val initialLoadingObservable = PublishRelay.create<Boolean>()

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, People>> {
        return apiService.getPeople(params.key ?: 1)
            .map<LoadResult<Int, People>> { response ->
                val peopleList = response.results?.map { it.toPeople() } ?: emptyList()
                repository.savePeopleListIntoDb(peopleList)
                initialLoadingObservable.accept(params.key == 1 || params.key == null)
                LoadResult.Page(
                    peopleList,
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