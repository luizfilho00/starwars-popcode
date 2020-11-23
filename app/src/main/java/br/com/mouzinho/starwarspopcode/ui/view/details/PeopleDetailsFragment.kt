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
import br.com.mouzinho.starwarspopcode.ui.view.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class PeopleDetailsFragment : Fragment(), Navigable {
    private val viewModel by viewModels<PeopleDetailViewModel>()
    private val activityViewModel by activityViewModels<MainViewModel>()
    private val people: People by lazy { arguments?.getSerializable(PEOPLE_EXTRA) as People }
    private var binding: FragmentPeopleDetailsBinding? = null
    private var favoriteItem: MenuItem? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPeopleDetailsBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        viewModel.loadDetails(people)
        subscribeUi()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_favorite, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_favorite) {
            favoriteItem = item
            return consume { viewModel.updateFavorite(people) }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupUi() {
        activityViewModel.onTitleChanged(people.name)
    }

    private fun subscribeUi() {
        binding?.run {
            lifecycleScope.launchWhenStarted {
                progressView.isVisible = true
                awaitAll(
                    async {
                        viewModel.peopleDetails.collect { people ->
                            textViewName.text = people.name
                            textViewBirth.text = people.birthYear
                            textViewHair.text = people.hairColor
                            textViewGender.text = people.gender
                            textViewHeight.text = people.height
                            textViewMass.text = people.mass
                            textViewSkin.text = people.skinColor
                            textViewHomeland.text = people.planet
                            textViewSpecie.text = people.speciesNames
                            progressView.isVisible = false
                        }
                    },
                    async { viewModel.favorited.collect(::onFavoriteUpdated) }
                )
            }
        }
    }

    private fun onFavoriteUpdated(favorited: Boolean) {
        favoriteItem?.setIcon(if (favorited) R.drawable.ic_star_filled else R.drawable.ic_star_border)
        Toast.makeText(
            requireContext(),
            if (favorited) R.string.favorite_saved_msg else R.string.favorite_removed_msg,
            Toast.LENGTH_SHORT
        ).show()
    }

    companion object {
        private const val PEOPLE_EXTRA = "PEOPLE_EXTRA"

        fun newInstance(people: People) = PeopleDetailsFragment().apply {
            arguments = bundleOf(PEOPLE_EXTRA to people)
        }
    }
}