package br.com.mouzinho.starwarspopcode.data.repository

import br.com.mouzinho.starwarspopcode.data.entity.DbPeople
import br.com.mouzinho.starwarspopcode.data.network.ApiService
import br.com.mouzinho.starwarspopcode.domain.entity.People
import br.com.mouzinho.starwarspopcode.domain.repository.PeopleRepository
import io.reactivex.Single
import io.realm.Realm
import io.realm.kotlin.where
import javax.inject.Inject

class PeopleRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val realm: Realm
) : PeopleRepository {

    override fun loadPlanetName(url: String): Single<String> {
        return apiService.getPlanetName(url).map { it.name ?: "" }
    }

    override fun loadSpecieName(url: String): Single<String> {
        return Single.just("")
    }

    override fun loadAllFromDb(): List<People> {
        return realm.where<DbPeople>()
            .findAll()
            .filter { it.isLoaded }
            .map { it.toPeople() }
    }

    override fun savePeopleListIntoDb(list: List<People>) {
        return realm.executeTransaction { realm ->
            realm.copyToRealmOrUpdate(list.map(DbPeople::fromPeople))
        }
    }

    override fun saveAsFavorite(people: People) {
        realm.executeTransaction { realm ->
            realm.copyToRealmOrUpdate(DbPeople.fromPeople(people).apply { favorite = true })
        }
    }

    override fun removeFromFavorites(people: People) {
        realm.executeTransaction { realm ->
            realm.copyToRealmOrUpdate(DbPeople.fromPeople(people).apply { favorite = false })
        }
    }

    override fun loadAllFavorites(): List<People> {
        return realm
            .where<DbPeople>()
            .equalTo(DbPeople.FIELD_FAVORITE, true)
            .findAll()
            .filter { it.isLoaded }
            .map { it.toPeople() }
    }
}