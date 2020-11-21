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
    private val apiService: ApiService
) : PeopleRepository {

    override fun loadPlanetName(url: String): Single<String> {
        return apiService.getPlanetName(url).map { it.name ?: "" }
    }

    override fun loadSpecieName(url: String): Single<String> {
        return Single.just("")
    }

    override fun loadAllFromDb(): List<People> {
        return Realm.getDefaultInstance().where<DbPeople>()
            .findAll()
            .filter { it.isLoaded }
            .map { it.toPeople() }
    }

    override fun savePeopleListIntoDb(list: List<People>) {
        return Realm.getDefaultInstance().executeTransaction { realm ->
            realm.copyToRealmOrUpdate(list.map(DbPeople::fromPeople))
        }
    }
}