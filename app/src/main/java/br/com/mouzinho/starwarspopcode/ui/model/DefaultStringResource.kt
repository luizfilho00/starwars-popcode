package br.com.mouzinho.starwarspopcode.ui.model

import android.content.Context
import br.com.mouzinho.starwarspopcode.R
import br.com.mouzinho.starwarspopcode.domain.util.StringResource
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DefaultStringResource @Inject constructor(
    @ApplicationContext private val appContext: Context
) : StringResource {

    override val toolbarTitle: String get() = appContext.getString(R.string.app_name)
    override val defaultErrorMessage: String get() = appContext.getString(R.string.default_error)
    override val removedFavoriteMessage: String get() = appContext.getString(R.string.favorite_removed_msg)
    override val savedFavoriteMessage: String get() = appContext.getString(R.string.favorite_saved_msg)
}