package br.com.mouzinho.starwarspopcode.presentation.view.main

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.mouzinho.starwarspopcode.model.People

class PeopleAdapter : RecyclerView.Adapter<PeopleViewHolder>() {
    private val data = mutableListOf<People>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeopleViewHolder {
        return PeopleViewHolder.inflate(parent)
    }

    override fun onBindViewHolder(holder: PeopleViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun submitList(list: List<People>) {
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }
}

