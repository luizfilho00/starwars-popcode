package br.com.mouzinho.starwarspopcode.ui.view.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.mouzinho.starwarspopcode.R
import br.com.mouzinho.starwarspopcode.databinding.ViewHolderPeopleBinding
import br.com.mouzinho.starwarspopcode.domain.entity.People

class FavoritesViewHolder(
    private val binding: ViewHolderPeopleBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(people: People?, removeFavoriteListener: (People) -> Unit) {
        with(binding) {
            textViewName.text = people?.name
            imageViewFavorite.setImageResource(R.drawable.ic_star_filled)
            imageViewFavorite.setOnClickListener { people?.let(removeFavoriteListener::invoke) }
        }
    }

    companion object {
        fun inflate(parent: ViewGroup) = FavoritesViewHolder(
            ViewHolderPeopleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }
}