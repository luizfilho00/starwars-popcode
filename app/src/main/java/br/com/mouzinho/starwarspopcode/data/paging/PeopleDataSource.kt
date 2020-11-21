package br.com.mouzinho.starwarspopcode.data.paging

import androidx.paging.rxjava2.RxPagingSource
import br.com.mouzinho.starwarspopcode.domain.entity.People
import br.com.mouzinho.starwarspopcode.domain.repository.PeopleRepository
import io.reactivex.Single
import javax.inject.Inject

class PeopleDataSource @Inject constructor(
    private val repository: PeopleRepository
) : RxPagingSource<Int, People>() {

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, People>> {
        return Single.just<LoadResult<Int, People>>(LoadResult.Page(emptyList(), null, null))
            .flatMap {
                repository.getAll(params.key ?: 1)
                    .map { response ->
                        LoadResult.Page(
                            response.results?.map(People::fromApiPeople) ?: emptyList(),
                            response.previous?.substringAfter("=")?.toIntOrNull(),
                            response.next?.substringAfter("=")?.toIntOrNull()
                        )
                    }
                    .onErrorReturnItem(
                        LoadResult.Page(
                            emptyList(),
                            null,
                            null
                        )
                    )
            }
    }
}