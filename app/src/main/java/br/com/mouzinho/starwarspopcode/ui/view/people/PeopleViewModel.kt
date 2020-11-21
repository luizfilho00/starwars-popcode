package br.com.mouzinho.starwarspopcode.ui.view.people

import androidx.hilt.lifecycle.ViewModelInject
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava2.observable
import br.com.mouzinho.starwarspopcode.data.paging.PeopleDataSource
import br.com.mouzinho.starwarspopcode.domain.entity.People
import br.com.mouzinho.starwarspopcode.ui.util.BaseViewModel
import br.com.mouzinho.starwarspopcode.ui.util.Constants
import br.com.mouzinho.starwarspopcode.ui.util.SchedulerProvider
import io.reactivex.Observable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.PublishSubject

class PeopleViewModel @ViewModelInject constructor(
    private val schedulerProvider: SchedulerProvider,
    private val peopleDataSource: PeopleDataSource
) : BaseViewModel() {
    val peopleObservable: Observable<PagingData<People>> by lazy { peoplePublisher.hide() }
    val loadingObservable: Observable<Boolean> by lazy { loadingPublisher.hide() }
    val errorObservable: Observable<String> by lazy { errorPublisher.hide() }

    private val peoplePublisher by lazy { PublishSubject.create<PagingData<People>>() }
    private val loadingPublisher by lazy { PublishSubject.create<Boolean>() }
    private val errorPublisher by lazy { PublishSubject.create<String>() }

    init {
        observeInitialPageLoading()
        observePagingData()
    }

    private fun observeInitialPageLoading() {
        peopleDataSource
            .initialLoadingObservable
            .observeOn(schedulerProvider.ui())
            .subscribe(loadingPublisher::onNext)
            .addTo(disposables)
    }

    private fun observePagingData() {
        Pager(PagingConfig(10, enablePlaceholders = false)) { peopleDataSource }
            .observable
            .observeOn(schedulerProvider.ui())
            .subscribe(peoplePublisher::onNext) { errorPublisher.onNext(it.message ?: Constants.defaultErrorMessage) }
            .addTo(disposables)
    }
}