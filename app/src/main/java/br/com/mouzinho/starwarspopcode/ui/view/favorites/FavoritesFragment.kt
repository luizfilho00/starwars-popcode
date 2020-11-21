package br.com.mouzinho.starwarspopcode.ui.view.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import br.com.mouzinho.starwarspopcode.databinding.FragmentFavoritesBinding
import br.com.mouzinho.starwarspopcode.ui.util.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxkotlin.addTo

@AndroidEntryPoint
class FavoritesFragment : BaseFragment() {
    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var adapter: FavoritesAdapter
    private val viewModel by viewModels<FavoritesViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        subscribeUi()
    }

    private fun setupUi() {
        if (!::adapter.isInitialized) adapter = FavoritesAdapter()
        binding.recyclerView.adapter = adapter
    }

    private fun subscribeUi() {
        viewModel.favoritesObservable
            .subscribe {
                adapter.submitList(it)
                binding.layoutEmpty.isVisible = it.isEmpty()
            }
            .addTo(disposables)
    }
}