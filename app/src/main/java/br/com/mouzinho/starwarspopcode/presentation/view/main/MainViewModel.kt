package br.com.mouzinho.starwarspopcode.presentation.view.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import br.com.mouzinho.starwarspopcode.model.People
import br.com.mouzinho.starwarspopcode.presentation.util.scheduler.SchedulerProvider
import br.com.mouzinho.starwarspopcode.repository.PeopleRepository
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

class MainViewModel @ViewModelInject constructor(
    private val repository: PeopleRepository,
    private val schedulerProvider: SchedulerProvider
) : ViewModel() {

    val stateObservable: Observable<MainViewState> by lazy { statePublisher.hide() }
    private val statePublisher = PublishSubject.create<MainViewState>()
    private val disposables = CompositeDisposable()

    fun loadPeople() {
        repository.getPeople(1)
            .observeOn(schedulerProvider.main())
            .doOnSubscribe { showLoading() }
            .doAfterTerminate { hideLoading() }
            .subscribe(::onPeopleReceived, ::onError)
            .let(disposables::add)
    }

    private fun showLoading() {
        statePublisher.onNext(MainViewState.ShowLoading)
    }

    private fun hideLoading() {
        statePublisher.onNext(MainViewState.HideLoading)
    }

    private fun onPeopleReceived(people: List<People>) {
        statePublisher.onNext(MainViewState.ShowPeople(people))
    }

    private fun onError(throwable: Throwable) {
        statePublisher.onNext(MainViewState.ShowError(throwable.message))
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}