package br.com.mouzinho.starwarspopcode.data.paging

import androidx.paging.DataSource
import br.com.mouzinho.starwarspopcode.data.dao.DbPeopleDao
import br.com.mouzinho.starwarspopcode.data.entity.DbPeople
import javax.inject.Inject

class PagingDataSource @Inject constructor(
    private val dao: DbPeopleDao
) : DataSource.Factory<Int, DbPeople>() {
    var query = ""

    override fun create(): DataSource<Int, DbPeople> {
        return if (query.isEmpty())
            dao.getAllPeople().create()
        else dao.searchPeopleByName("%$query%").create()
    }
}