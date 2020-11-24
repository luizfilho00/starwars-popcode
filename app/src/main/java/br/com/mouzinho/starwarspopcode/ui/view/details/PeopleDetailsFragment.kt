package br.com.mouzinho.starwarspopcode.ui.view.details

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import br.com.mouzinho.starwarspopcode.R
import br.com.mouzinho.starwarspopcode.databinding.FragmentPeopleDetailsBinding
import br.com.mouzinho.starwarspopcode.domain.entity.People
import br.com.mouzinho.starwarspopcode.ui.navigation.Navigable
import br.com.mouzinho.starwarspopcode.ui.util.consume
import br.com.mouzinho.starwarspopcode.ui.view.main.MainViewAction
import br.com.mouzinho.starwarspopcode.ui.view.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class PeopleDetailsFragment : Fragment(), Navigable {
    private val viewModel by viewModels<PeopleDetailsViewModel>()
    private val activityViewModel by activityViewModels<MainViewModel>()
    private val people: People by lazy { arguments?.getSerializable(PEOPLE_EXTRA) as People }
    private var binding: FragmentPeopleDetailsBinding? = null
    private var favoriteItem: MenuItem? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPeopleDetailsBinding.inflate(inflater, container, false)
        binding?.lifecycleOwner = viewLifecycleOwner
        setHasOptionsMenu(true)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        subscribeUi()
        viewModel.updateViewState(PeopleDetailsViewAction.LoadDetails(people))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_favorite, menu)
        favoriteItem = menu.findItem(R.id.action_favorite)
        updateFavoriteIcon(people.favorite)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_favorite)
            return consume { viewModel.updateViewState(PeopleDetailsViewAction.UpdateFavorite(people)) }
        return super.onOptionsItemSelected(item)
    }

    private fun setupUi() {
        activityViewModel.updateViewState(MainViewAction.ChangeToolbarTitle(people.name))
    }

    private fun subscribeUi() {
        lifecycleScope.launchWhenStarted {
            viewModel.viewState.collect(::render)
        }
    }

    private fun render(state: PeopleDetailsViewState) {
        if (state.savedAsFavorite != null)
            updateFavoriteIcon(state.savedAsFavorite)
        if (state.showFavoriteSaveMessage)
            Toast.makeText(requireContext(), state.favoriteSaveMessage, Toast.LENGTH_LONG).show()
        binding?.run {
            progressView.isVisible = state.isLoading
            if (state.peopleWithAllInformations != null)
                this@run.people = state.peopleWithAllInformations
        }
    }

    private fun updateFavoriteIcon(favorited: Boolean) {
        favoriteItem?.setIcon(if (favorited) R.drawable.ic_star_filled else R.drawable.ic_star_border)
    }

    companion object {
        private const val PEOPLE_EXTRA = "PEOPLE_EXTRA"

        fun newInstance(people: People) = PeopleDetailsFragment().apply {
            arguments = bundleOf(PEOPLE_EXTRA to people)
        }
    }
}