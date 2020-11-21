package br.com.mouzinho.starwarspopcode.ui.view.people

import androidx.hilt.lifecycle.ViewModelInject
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava2.observable
import br.com.mouzinho.starwarspopcode.data.paging.PeopleDataSource
import br.com.mouzinho.starwarspopcode.domain.entity.People
import br.com.mouzinho.starwarspopcode.ui.util.BaseViewModel
import br.com.mouzinho.starwarspopcode.ui.util.SchedulerProvider
import io.reactivex.Observable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.PublishSubject

class PeopleViewModel @ViewModelInject constructor(
    schedulerProvider: SchedulerProvider,
    private val peopleDataSource: PeopleDataSource
) : BaseViewModel() {
    val peopleObservable: Observable<PagingData<People>> by lazy { peoplePublisher.hide() }

    private val peoplePublisher by lazy { PublishSubject.create<PagingData<People>>() }

    init {
        Pager(PagingConfig(10, enablePlaceholders = false)) { peopleDataSource }
            .observable
            .observeOn(schedulerProvider.ui())
            .subscribeBy { peoplePublisher.onNext(it) }
            .addTo(disposables)
    }
}