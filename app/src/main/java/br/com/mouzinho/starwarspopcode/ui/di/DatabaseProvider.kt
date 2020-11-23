package br.com.mouzinho.starwarspopcode.ui.di

import android.content.Context
import br.com.mouzinho.starwarspopcode.ui.model.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object DatabaseProvider {

    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext context: Context) = AppDatabase.build(context)

    @Provides
    @Singleton
    fun providesPeopleDao(database: AppDatabase) = database.peopleDao()

    @Provides
    @Singleton
    fun providesRemoteKeyDao(database: AppDatabase) = database.remoteKeyDao()
}