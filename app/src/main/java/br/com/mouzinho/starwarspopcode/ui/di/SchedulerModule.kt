package br.com.mouzinho.starwarspopcode.ui.di

import br.com.mouzinho.starwarspopcode.ui.util.DefaultSchedulersProvider
import br.com.mouzinho.starwarspopcode.ui.util.SchedulerProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
interface SchedulerModule {

    @Binds
    fun bindsDefaultScheduler(impl: DefaultSchedulersProvider) : SchedulerProvider
}