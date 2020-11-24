package br.com.mouzinho.starwarspopcode.ui.view.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import br.com.mouzinho.starwarspopcode.R
import br.com.mouzinho.starwarspopcode.databinding.FragmentFavoritesBinding
import br.com.mouzinho.starwarspopcode.domain.entity.People
import br.com.mouzinho.starwarspopcode.people
import br.com.mouzinho.starwarspopcode.ui.navigation.Navigable
import br.com.mouzinho.starwarspopcode.ui.navigation.Navigator
import br.com.mouzinho.starwarspopcode.ui.view.details.PeopleDetailsFragment
import br.com.mouzinho.starwarspopcode.ui.view.main.MainViewModel
import br.com.mouzinho.starwarspopcode.ui.view.main.MainViewState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoritesFragment : Fragment(), Navigable {
    private var binding: FragmentFavoritesBinding? = null
    private val viewModel by viewModels<FavoritesViewModel>()
    private val activityViewModel by activityViewModels<MainViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeUi()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun subscribeUi() {
        lifecycleScope.launchWhenStarted {
            awaitAll(
                async { viewModel.viewState.collect(::render) },
                async { activityViewModel.mainViewState.collect(::onMainViewStateUpdate) }
            )
        }
    }

    private fun onMainViewStateUpdate(state: MainViewState) {
        viewModel.updateViewState(FavoritesViewAction.Search(state.searchText))
    }

    private fun render(state: FavoritesViewState) {
        binding?.run {
            layoutEmpty.isVisible = state.favorites.isEmpty()
            progressView.isVisible = state.isLoading
            if (state.favorites.isNotEmpty())
                onFavoritesReceived(state.favorites)
        }
    }

    private fun onFavoritesReceived(favorites: List<People>) {
        binding?.recyclerView?.run {
            setRemoveAdapterWhenDetachedFromWindow(true)
            withModels {
                favorites.forEach { favoritePeople ->
                    people {
                        id(favoritePeople.id)
                        people(favoritePeople)
                        favorited(true)
                        onFavoriteClickListener(View.OnClickListener { onRemoveFavorite(favoritePeople) })
                        onViewClickListener(View.OnClickListener { onGoToDetails(favoritePeople) })
                    }
                }
            }
        }
    }

    private fun onRemoveFavorite(people: People) {
        viewModel.updateViewState(FavoritesViewAction.RemoveFavorite(people))
        Toast.makeText(requireContext(), R.string.favorite_removed_msg, Toast.LENGTH_SHORT).show()
    }

    private fun onGoToDetails(people: People) {
        lifecycleScope.launch {
            Navigator.push(PeopleDetailsFragment.newInstance(people))
        }
    }
}