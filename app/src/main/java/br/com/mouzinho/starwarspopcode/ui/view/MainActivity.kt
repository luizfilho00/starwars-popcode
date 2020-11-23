package br.com.mouzinho.starwarspopcode.ui.view

import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import br.com.mouzinho.starwarspopcode.R
import br.com.mouzinho.starwarspopcode.databinding.ActivityMainBinding
import br.com.mouzinho.starwarspopcode.ui.navigation.Navigable
import br.com.mouzinho.starwarspopcode.ui.navigation.Navigator
import br.com.mouzinho.starwarspopcode.ui.view.favorites.FavoritesFragment
import br.com.mouzinho.starwarspopcode.ui.view.people.PeopleFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupNavigation()
        setupUi()
        showInitialFragment()
    }

    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.findFragmentById(binding.frameLayoutContainer.id)
        if (currentFragment is Navigable) lifecycleScope.launchWhenStarted { currentFragment.onBackPressed() }
        else super.onBackPressed()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        setupSearchView(menu.findItem(R.id.app_bar_search).actionView as SearchView)
        return super.onCreateOptionsMenu(menu)
    }

    private fun setupNavigation() {
        binding.bottomNavigation.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_people -> {
                    supportFragmentManager.findFragmentById(binding.frameLayoutContainer.id)?.let {
                        supportFragmentManager.beginTransaction().remove(it).commit()
                    }
                    supportFragmentManager.beginTransaction().add(binding.frameLayoutContainer.id, PeopleFragment()).commit()
                    true
                }
                R.id.action_favorites -> {
                    supportFragmentManager.findFragmentById(binding.frameLayoutContainer.id)?.let {
                        supportFragmentManager.beginTransaction().remove(it).commit()
                    }
                    supportFragmentManager.beginTransaction().add(binding.frameLayoutContainer.id, FavoritesFragment())
                        .commit()
                    true
                }
                else -> false
            }
        }
    }

    private fun setupUi() {
        lifecycleScope.launch(Dispatchers.Main) {
            Navigator
                .navObservable
                .collect { action ->
                    when (action) {
                        is Navigator.NavAction.Push -> onPushAction(action)
                        is Navigator.NavAction.PopUp -> super.onBackPressed()
                        is Navigator.NavAction.PopUpTo -> onPopUpToAction(action)
                    }
                }
        }
    }

    private fun onPushAction(action: Navigator.NavAction.Push) {
        supportFragmentManager.beginTransaction()
            .add(binding.frameLayoutContainer.id, action.fragment)
            .addToBackStack(action.fragment.javaClass.simpleName)
            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right)
            .commit()
    }

    private fun onPopUpToAction(action: Navigator.NavAction.PopUpTo) {
        supportFragmentManager.popBackStack(action.fragment.javaClass.simpleName, 0)
    }

    private fun setupSearchView(searchView: SearchView) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false
            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { viewModel.onSearch(it) }
                return false
            }
        })
    }

    private fun showInitialFragment() {
        supportFragmentManager.beginTransaction().add(binding.frameLayoutContainer.id, PeopleFragment()).commit()
    }
}