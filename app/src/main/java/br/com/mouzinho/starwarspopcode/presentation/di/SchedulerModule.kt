package br.com.mouzinho.starwarspopcode.presentation.di

import br.com.mouzinho.starwarspopcode.presentation.util.scheduler.DefaultSchedulerProvider
import br.com.mouzinho.starwarspopcode.presentation.util.scheduler.SchedulerProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
interface SchedulerModule {

    @Binds
    fun bindsDefaultScheduler(impl: DefaultSchedulerProvider): SchedulerProvider
}