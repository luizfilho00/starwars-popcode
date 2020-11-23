package br.com.mouzinho.starwarspopcode.ui.view.people

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import br.com.mouzinho.starwarspopcode.R
import br.com.mouzinho.starwarspopcode.databinding.FragmentPeopleEpoxyBinding
import br.com.mouzinho.starwarspopcode.domain.entity.People
import br.com.mouzinho.starwarspopcode.ui.navigation.Navigator
import br.com.mouzinho.starwarspopcode.ui.view.details.PeopleDetailsFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class PeopleFragment : Fragment() {
    private var binding: FragmentPeopleEpoxyBinding? = null
    private val viewModel by viewModels<PeopleViewModel>()
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

    private fun onPeopleClick(people: People) {
        lifecycleScope.launchWhenStarted {
            Navigator.push(PeopleDetailsFragment.newInstance(people))
        }
    }

    private fun subscribeUi() {
        viewModel.peopleLiveData.observe(viewLifecycleOwner) { epoxyController.submitList(it) }
        lifecycleScope.launchWhenStarted {
            awaitAll(
                async { viewModel.loadingObservable.collect { epoxyController.isLoading = it } },
                async {
                    viewModel.favoriteObservable.collect { favorited ->
                        Toast.makeText(
                            requireContext(),
                            if (favorited) R.string.favorite_saved_msg else R.string.favorite_removed_msg,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            )
        }
    }

    private fun onFavoriteClick(people: People) {
        viewModel.updateFavorite(people)
    }

    private fun setupEpoxy() {
        binding?.recyclerView?.run {
            adapter = epoxyController.adapter
            setRemoveAdapterWhenDetachedFromWindow(true)
        }
    }

}