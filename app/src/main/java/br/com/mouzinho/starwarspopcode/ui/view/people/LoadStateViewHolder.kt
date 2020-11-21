package br.com.mouzinho.starwarspopcode.ui.view.people

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.mouzinho.starwarspopcode.R
import br.com.mouzinho.starwarspopcode.databinding.ItemLoadingBinding

class PeopleLoadStateAdapter : LoadStateAdapter<LoadStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState) =
        LoadStateViewHolder(parent)

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }
}

class LoadStateViewHolder(
    parent: ViewGroup
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_loading, parent, false)
) {
    private val binding = ItemLoadingBinding.bind(itemView)

    fun bind(loadState: LoadState) {
        binding.textViewError.isVisible = loadState is LoadState.Error
        binding.progress.isVisible = loadState is LoadState.Loading
    }
}
