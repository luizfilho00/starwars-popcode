package br.com.mouzinho.starwarspopcode.ui.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import io.realm.Realm
import io.realm.RealmConfiguration
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object DatabaseProvider {

    @Provides
    @Singleton
    fun providesRealm(): Realm = Realm.getInstance(RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build())
}