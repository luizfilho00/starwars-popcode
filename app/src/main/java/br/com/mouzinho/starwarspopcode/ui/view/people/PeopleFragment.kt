package br.com.mouzinho.starwarspopcode.ui.view.people

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import br.com.mouzinho.starwarspopcode.R
import br.com.mouzinho.starwarspopcode.databinding.FragmentPeopleEpoxyBinding
import br.com.mouzinho.starwarspopcode.domain.entity.People
import br.com.mouzinho.starwarspopcode.ui.base.ScrollableFragment
import br.com.mouzinho.starwarspopcode.ui.navigation.Navigator
import br.com.mouzinho.starwarspopcode.ui.view.details.PeopleDetailsFragment
import br.com.mouzinho.starwarspopcode.ui.view.main.MainViewModel
import br.com.mouzinho.starwarspopcode.ui.view.main.MainViewState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class PeopleFragment : Fragment(), ScrollableFragment {
    private var binding: FragmentPeopleEpoxyBinding? = null
    private val viewModel by viewModels<PeopleViewModel>()
    private val activityViewModel by activityViewModels<MainViewModel>()
    private val epoxyController by lazy { PeoplePagingController(::onPeopleClick, ::onFavoriteClick) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPeopleEpoxyBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupEpoxy()
        subscribeUi()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun scrollToTop() {
        binding?.recyclerView?.smoothScrollToPosition(0)
    }

    private fun onPeopleClick(people: People) {
        lifecycleScope.launchWhenStarted {
            Navigator.push(PeopleDetailsFragment.newInstance(people))
        }
    }

    private fun subscribeUi() {
        lifecycleScope.launchWhenStarted {
            awaitAll(
                async { viewModel.viewState.collect(::renderState) },
                async { activityViewModel.mainViewState.collect(::onMainViewStateUpdate) }
            )
        }
    }

    private fun renderState(state: PeopleViewState) {
        if (state.peopleListUpdated)
            epoxyController.submitList(state.peopleList)
        if (state.favoriteSaved != null) {
            Toast.makeText(
                requireContext(),
                if (state.favoriteSaved) R.string.favorite_saved_msg else R.string.favorite_removed_msg,
                Toast.LENGTH_SHORT
            ).show()
        }
        epoxyController.isLoading = state.isLoading
    }

    private fun onFavoriteClick(people: People) {
        viewModel.updateViewState(PeopleViewAction.UpdateFavorite(people))
    }

    private fun onMainViewStateUpdate(mainViewState: MainViewState) {
        viewModel.updateViewState(PeopleViewAction.Search(mainViewState.searchText))
    }

    private fun setupEpoxy() {
        binding?.recyclerView?.run {
            adapter = epoxyController.adapter
            setRemoveAdapterWhenDetachedFromWindow(true)
        }
    }

}