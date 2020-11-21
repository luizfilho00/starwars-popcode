package br.com.mouzinho.starwarspopcode.ui.di

import br.com.mouzinho.starwarspopcode.data.repository.PeopleRepositoryImpl
import br.com.mouzinho.starwarspopcode.domain.repository.PeopleRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
interface RepositoryModule {

    @Binds
    fun bindsPeopleRepository(impl: PeopleRepositoryImpl) : PeopleRepository
}