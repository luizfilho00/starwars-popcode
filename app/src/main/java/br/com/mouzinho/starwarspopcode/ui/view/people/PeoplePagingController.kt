package br.com.mouzinho.starwarspopcode.ui.view.people

import android.view.View
import br.com.mouzinho.starwarspopcode.LoadingBindingModel_
import br.com.mouzinho.starwarspopcode.PeopleBindingModel_
import br.com.mouzinho.starwarspopcode.domain.entity.People
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.paging.PagedListEpoxyController
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PeoplePagingController @Inject constructor(
    private val onViewClick: (People) -> Unit,
    private val onFavoriteClick: (People) -> Unit,
) : PagedListEpoxyController<People>() {
    var isLoading = false
        set(value) {
            field = value
            requestModelBuild()
        }

    init {
        isDebugLoggingEnabled = true
    }

    override fun buildItemModel(currentPosition: Int, item: People?): EpoxyModel<*> {
        return if (item == null) {
            LoadingBindingModel_().id(LOADING_ID)
        } else {
            PeopleBindingModel_()
                .id(item.id)
                .name("$currentPosition: ${item.name}")
                .favorited(item.favorite)
                .onFavoriteClickListener(View.OnClickListener { onFavoriteClick(item) })
                .onViewClickListener(View.OnClickListener { onViewClick(item) })
        }
    }

    override fun addModels(models: List<EpoxyModel<*>>) {
        if (isLoading) {
            super.addModels(models.plus(LoadingBindingModel_().id(LOADING_ID)).distinct())
        } else
            super.addModels(models.distinct())
    }

    companion object {
        private const val LOADING_ID = "loading"
    }
}