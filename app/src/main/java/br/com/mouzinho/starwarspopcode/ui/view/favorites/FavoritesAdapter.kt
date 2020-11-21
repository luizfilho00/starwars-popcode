package br.com.mouzinho.starwarspopcode.ui.view.favorites

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import br.com.mouzinho.starwarspopcode.domain.entity.People

class FavoritesAdapter : ListAdapter<People, FavoritesViewHolder>(
    object : DiffUtil.ItemCallback<People>() {
        override fun areItemsTheSame(oldItem: People, newItem: People) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: People, newItem: People) = oldItem == newItem
    }
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        FavoritesViewHolder.inflate(parent)

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}