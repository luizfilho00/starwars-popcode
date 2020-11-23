package br.com.mouzinho.starwarspopcode.data.paging

import androidx.paging.DataSource
import br.com.mouzinho.starwarspopcode.data.dao.DbPeopleDao
import br.com.mouzinho.starwarspopcode.domain.entity.People
import javax.inject.Inject

class PagingDataSource @Inject constructor(
    private val dao: DbPeopleDao
) : DataSource.Factory<Int, People>() {
    var query = ""
    private var dataSource: DataSource<Int, People>? = null

    fun invalidate() = dataSource?.invalidate()

    override fun create(): DataSource<Int, People> {
        dataSource = if (query.isEmpty())
            dao.getAllPeople().map { it.toPeople() }.create()
        else dao.searchPeopleByName("%$query%").map { it.toPeople() }.create()
        return dataSource!!
    }
}