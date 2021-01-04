package br.com.mouzinho.starwarspopcode.presentation.view.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import br.com.mouzinho.starwarspopcode.R
import br.com.mouzinho.starwarspopcode.databinding.ActivityMainBinding
import br.com.mouzinho.starwarspopcode.model.People
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: PeopleAdapter
    private val viewModel by viewModels<MainViewModel>()
    private val disposables = CompositeDisposable()

    override fun onBackPressed() {
        super.finishAfterTransition()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setupUi()
        observeViewState()
        viewModel.loadPeople()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

    private fun setupUi() {
        adapter = PeopleAdapter()
        binding.recyclerView.adapter = adapter
    }

    private fun observeViewState() {
        viewModel
            .stateObservable
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::onNextState)
            .let(disposables::add)
    }

    private fun onNextState(state: MainViewState) {
        when (state) {
            is MainViewState.ShowLoading -> showLoading()
            is MainViewState.HideLoading -> hideLoading()
            is MainViewState.ShowPeople -> showPeople(state.list)
            is MainViewState.ShowError -> showError(state.message)
        }
    }

    private fun showLoading() {
        binding.progressView.isVisible = true
    }

    private fun hideLoading() {
        binding.progressView.isVisible = false
    }

    private fun showPeople(people: List<People>) {
        adapter.submitList(people)
    }

    private fun showError(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}