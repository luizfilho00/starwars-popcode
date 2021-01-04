package br.com.mouzinho.starwarspopcode.presentation.di

import br.com.mouzinho.starwarspopcode.repository.PeopleRepositoryImpl
import br.com.mouzinho.starwarspopcode.repository.PeopleRepository
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