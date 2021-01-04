package br.com.mouzinho.starwarspopcode.presentation.view.main

import br.com.mouzinho.starwarspopcode.presentation.util.scheduler.SchedulerProvider
import br.com.mouzinho.starwarspopcode.repository.PeopleRepository
import com.nhaarman.mockitokotlin2.any
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.TestSubscriber
import net.bytebuddy.implementation.bytecode.Throw
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class MainViewModelTest {
    lateinit var viewModel: MainViewModel
    var testObserver: TestObserver<MainViewState> = TestObserver()

    @Mock
    lateinit var repository: PeopleRepository
    @Mock
    lateinit var schedulerProvider: SchedulerProvider

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        viewModel = MainViewModel(repository, schedulerProvider)
    }

    @Test
    fun `Test getPeople And Send ShowLoading State`() {
        Mockito
            .`when`(repository.getPeople(any()))
            .thenReturn(Observable.just(emptyList()))
        Mockito
            .`when`(schedulerProvider.main())
            .thenReturn(Schedulers.trampoline())
        viewModel.stateObservable.subscribe(testObserver)
        viewModel.loadPeople()
        testObserver.assertValueAt(0) {
            it is MainViewState.ShowLoading
        }
    }

    @Test
    fun `Test getPeople And Send ShowError State`() {
        Mockito
            .`when`(repository.getPeople(any()))
            .thenReturn(Observable.error(Throwable("Oops, aconteceu algo errado")))
        Mockito
            .`when`(schedulerProvider.main())
            .thenReturn(Schedulers.trampoline())
        viewModel.stateObservable.subscribe(testObserver)
        viewModel.loadPeople()
        testObserver.assertValueAt(0) {
            it is MainViewState.ShowLoading
        }
        testObserver.assertValueAt(1) {
            it is MainViewState.ShowError
        }
        testObserver.assertValueAt(2) {
            it is MainViewState.HideLoading
        }
    }
}