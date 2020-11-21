package br.com.mouzinho.starwarspopcode.ui.navigation

import androidx.fragment.app.Fragment
import com.jakewharton.rxrelay2.PublishRelay

object Navigator {
    sealed class NavAction {
        data class Push(val fragment: Fragment) : NavAction()
        data class PopUpTo(val fragment: Fragment) : NavAction()
        object PopUp : NavAction()
    }

    val navObservable = PublishRelay.create<NavAction>()

    fun popUp() = navObservable.accept(NavAction.PopUp)

    fun push(fragment: Fragment) = navObservable.accept(NavAction.Push(fragment))

    fun popUpTo(fragment: Fragment) = navObservable.accept(NavAction.PopUpTo(fragment))
}