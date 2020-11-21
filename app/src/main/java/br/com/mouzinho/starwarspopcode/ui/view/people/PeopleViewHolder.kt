package br.com.mouzinho.starwarspopcode.ui.view.people

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.mouzinho.starwarspopcode.databinding.ItemPeopleBinding
import br.com.mouzinho.starwarspopcode.domain.entity.People

class PeopleViewHolder(
    private val binding: ItemPeopleBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(people: People?) {
        binding.textViewName.text = people?.name
    }

    companion object {
        fun inflate(parent: ViewGroup) = PeopleViewHolder(
            ItemPeopleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }
}