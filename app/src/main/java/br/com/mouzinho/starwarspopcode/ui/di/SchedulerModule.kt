package br.com.mouzinho.starwarspopcode.ui.di

import br.com.mouzinho.starwarspopcode.ui.util.DefaultDispatcherProvider
import br.com.mouzinho.starwarspopcode.ui.util.DispatcherProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
interface SchedulerModule {

    @Binds
    fun bindsDefaultScheduler(impl: DefaultDispatcherProvider) : DispatcherProvider
}