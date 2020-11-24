package br.com.mouzinho.starwarspopcode.domain.useCase

import br.com.mouzinho.starwarspopcode.domain.entity.People
import br.com.mouzinho.starwarspopcode.domain.entity.SaveFavoriteResponse
import br.com.mouzinho.starwarspopcode.domain.repository.PeopleRepository
import java.util.*
import javax.inject.Inject
import kotlin.random.Random

class UpdateFavorite @Inject constructor(
    private val repository: PeopleRepository
) {

    suspend fun execute(people: People): SaveFavoriteResponse {
        val isToSaveOnFavorites = !(repository.getPeopleById(people.id)?.favorite ?: false)
        return if (isToSaveOnFavorites) {
            repository.updatePeopleAsFavoriteOnNetwork(
                people.copy(favorite = isToSaveOnFavorites),
                //Requisito -> Forçar erro na API em algumas requests aleatoriamente
                if (Random(Date().time).nextBoolean()) ERROR_ON_SAVE else null
            )
        } else {
            repository.updatePeopleAsFavoriteLocally(people.copy(favorite = isToSaveOnFavorites))
        }
    }

    companion object {
        private const val ERROR_ON_SAVE = 400
    }
}