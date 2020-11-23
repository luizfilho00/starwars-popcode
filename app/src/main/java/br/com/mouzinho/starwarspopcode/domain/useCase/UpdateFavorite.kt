package br.com.mouzinho.starwarspopcode.domain.useCase

import br.com.mouzinho.starwarspopcode.domain.entity.People
import br.com.mouzinho.starwarspopcode.domain.repository.PeopleRepository
import javax.inject.Inject

class UpdateFavorite @Inject constructor(
    private val repository: PeopleRepository
) {

    suspend fun execute(people: People): Boolean {
        val isFavorite = !(repository.getPeopleById(people.id)?.favorite ?: false)
        repository.updatePeople(people.copy(favorite = isFavorite))
        return isFavorite
    }
}