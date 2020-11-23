package br.com.mouzinho.starwarspopcode.ui.navigation

import androidx.fragment.app.Fragment
import kotlinx.coroutines.flow.MutableSharedFlow

object Navigator {
    sealed class NavAction {
        data class Push(val fragment: Fragment) : NavAction()
        data class PopUpTo(val fragment: Fragment) : NavAction()
        object PopUp : NavAction()
    }

    val navObservable = MutableSharedFlow<NavAction>()

    suspend fun popUp() = navObservable.emit(NavAction.PopUp)

    suspend fun push(fragment: Fragment) = navObservable.emit(NavAction.Push(fragment))

    suspend fun popUpTo(fragment: Fragment) = navObservable.emit(NavAction.PopUpTo(fragment))
}