package br.com.mouzinho.starwarspopcode.ui.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import br.com.mouzinho.starwarspopcode.R
import br.com.mouzinho.starwarspopcode.databinding.ActivityMainBinding
import br.com.mouzinho.starwarspopcode.ui.base.ScrollableFragment
import br.com.mouzinho.starwarspopcode.ui.navigation.Navigable
import br.com.mouzinho.starwarspopcode.ui.navigation.Navigator
import br.com.mouzinho.starwarspopcode.ui.util.consume
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
    private val defaultFragments = listOf(PeopleFragment(), FavoritesFragment())
    private var selectedFragment: Fragment = defaultFragments.first()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupNavigation()
        setupUi()
        showFragments()
        subscribeUi()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.fragments.size == defaultFragments.size && selectedFragment != defaultFragments[PEOPLE_FRAGMENT]) {
            onFragmentSelected(defaultFragments[PEOPLE_FRAGMENT])
            binding.bottomNavigation.selectedItemId = R.id.action_people
            return
        }
        val currentFragment = supportFragmentManager.findFragmentById(binding.frameLayoutContainer.id)
        if (currentFragment is Navigable)
            lifecycleScope.launchWhenStarted { currentFragment.onBackPressed() }
        else
            super.onBackPressed()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        setupSearchView(menu.findItem(R.id.app_bar_search).actionView as SearchView)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            return consume { onBackPressed() }
        return super.onOptionsItemSelected(item)
    }

    private fun setupNavigation() {
        setupBottomNavigation()
        binding.bottomNavigation.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_people -> consume { onFragmentSelected(defaultFragments[PEOPLE_FRAGMENT]) }
                R.id.action_favorites -> consume { onFragmentSelected(defaultFragments[FAVORITES_FRAGMENT]) }
                else -> false
            }
        }
    }

    private fun setupBottomNavigation() {
        supportFragmentManager.addOnBackStackChangedListener {
            supportActionBar?.run {
                val hasMoreFragmentsThanDefault = supportFragmentManager.fragments.size > defaultFragments.size
                setDisplayHomeAsUpEnabled(hasMoreFragmentsThanDefault)
                setDisplayShowHomeEnabled(hasMoreFragmentsThanDefault)
                if (!hasMoreFragmentsThanDefault) setTitle(R.string.app_name)
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

    private fun showFragments() {
        with(supportFragmentManager) {
            beginTransaction()
                .add(binding.frameLayoutContainer.id, defaultFragments[PEOPLE_FRAGMENT])
                .commit()
            beginTransaction()
                .add(binding.frameLayoutContainer.id, defaultFragments[FAVORITES_FRAGMENT])
                .hide(defaultFragments[FAVORITES_FRAGMENT])
                .commit()
        }
    }

    private fun onFragmentSelected(fragment: Fragment) {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.frame_layout_container)
        if (currentFragment != defaultFragments[PEOPLE_FRAGMENT] && currentFragment != defaultFragments[FAVORITES_FRAGMENT])
            supportFragmentManager.popBackStack()
        if (selectedFragment == fragment && fragment is ScrollableFragment)
            fragment.scrollToTop()
        supportFragmentManager
            .beginTransaction()
            .hide(selectedFragment)
            .show(fragment)
            .commit()
        selectedFragment = fragment
    }

    private fun subscribeUi() {
        lifecycleScope.launchWhenStarted {
            viewModel.title.collect { supportActionBar?.title = it }
        }
    }

    companion object {
        private const val PEOPLE_FRAGMENT = 0
        private const val FAVORITES_FRAGMENT = 1
    }
}