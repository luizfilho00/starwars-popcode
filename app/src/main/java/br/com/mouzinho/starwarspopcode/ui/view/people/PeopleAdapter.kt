package br.com.mouzinho.starwarspopcode.ui.view.people

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import br.com.mouzinho.starwarspopcode.domain.entity.People

class PeopleAdapter : PagingDataAdapter<People, PeopleViewHolder>(
    object : DiffUtil.ItemCallback<People>() {
        override fun areItemsTheSame(oldItem: People, newItem: People) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: People, newItem: People) = oldItem == newItem
    }
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PeopleViewHolder.inflate(parent)

    override fun onBindViewHolder(holder: PeopleViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}