package br.com.mouzinho.starwarspopcode.ui.view.people

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import br.com.mouzinho.starwarspopcode.databinding.FragmentPeopleBinding
import br.com.mouzinho.starwarspopcode.ui.util.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxkotlin.addTo

@AndroidEntryPoint
class PeopleFragment : BaseFragment() {
    private val viewModel by viewModels<PeopleViewModel>()
    private lateinit var binding: FragmentPeopleBinding
    private lateinit var adapter: PeopleAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPeopleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        subscribeUi()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.recyclerView.adapter = null
    }

    private fun subscribeUi() {
        viewModel.peopleObservable.subscribe { adapter.submitData(lifecycle, it) }
            .addTo(disposables)
    }

    private fun setupRecyclerView() {
        if (!::adapter.isInitialized) adapter = PeopleAdapter()
        binding.recyclerView.adapter = adapter.withLoadStateFooter(PeopleLoadStateAdapter())
    }
}