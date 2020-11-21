package br.com.mouzinho.starwarspopcode.ui.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.mouzinho.starwarspopcode.R
import br.com.mouzinho.starwarspopcode.databinding.ActivityMainBinding
import br.com.mouzinho.starwarspopcode.ui.navigation.Navigator
import br.com.mouzinho.starwarspopcode.ui.util.SchedulerProvider
import br.com.mouzinho.starwarspopcode.ui.view.favorites.FavoritesFragment
import br.com.mouzinho.starwarspopcode.ui.view.people.PeopleFragment
import com.pandora.bottomnavigator.BottomNavigator
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var schedulerProvider: SchedulerProvider

    private lateinit var binding: ActivityMainBinding
    private var firstFragment = true
    private val disposables = CompositeDisposable()
    private lateinit var navigator: BottomNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupNavigation()
        setupUi()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

    override fun onBackPressed() {
        if (!navigator.pop())
            super.onBackPressed()
    }

    private fun setupNavigation() {
        navigator = BottomNavigator.onCreate(
            fragmentContainer = binding.frameLayoutContainer.id,
            bottomNavigationView = binding.bottomNavigation,
            rootFragmentsFactory = mapOf(
                R.id.action_people to { PeopleFragment() },
                R.id.action_favorites to { FavoritesFragment() }
            ),
            defaultTab = R.id.action_people,
            activity = this
        )
    }

    private fun setupUi() {
        Navigator
            .navObservable
            .observeOn(schedulerProvider.ui())
            .subscribe { action ->
                when (action) {
                    is Navigator.NavAction.Push -> onPushAction(action)
                    is Navigator.NavAction.PopUp -> onBackPressed()
                    is Navigator.NavAction.PopUpTo -> onPopUpToAction(action)
                }
            }
            .addTo(disposables)
    }

    private fun onPushAction(action: Navigator.NavAction.Push) {
        supportFragmentManager.beginTransaction()
            .add(binding.frameLayoutContainer.id, action.fragment)
            .apply {
                if (firstFragment) {
                    firstFragment = false
                } else {
                    addToBackStack(action.fragment.javaClass.simpleName)
                }
            }
            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right)
            .commit()
    }

    private fun onPopUpToAction(action: Navigator.NavAction.PopUpTo) {
        supportFragmentManager.popBackStack(action.fragment.javaClass.simpleName, 0)
    }
}