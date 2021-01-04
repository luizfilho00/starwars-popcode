package br.com.mouzinho.starwarspopcode.presentation.view.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.mouzinho.starwarspopcode.databinding.ViewHolderPeopleBinding
import br.com.mouzinho.starwarspopcode.model.People

class PeopleViewHolder(private val binding: ViewHolderPeopleBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(people: People) {
        binding.people = people
    }

    companion object {
        fun inflate(parent: ViewGroup) = PeopleViewHolder(
            ViewHolderPeopleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
}