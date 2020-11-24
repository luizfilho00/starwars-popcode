package br.com.mouzinho.starwarspopcode.ui.di

import br.com.mouzinho.starwarspopcode.domain.util.StringResource
import br.com.mouzinho.starwarspopcode.ui.model.DefaultStringResource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
interface StringModule {

    @Binds
    fun bindsStringResouce(impl: DefaultStringResource): StringResource
}