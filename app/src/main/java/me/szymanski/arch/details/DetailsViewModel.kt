package me.szymanski.arch.details

import android.content.Context
import androidx.core.content.ContextCompat.getString
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import me.szymanski.arch.R
import me.szymanski.arch.domain.data.DetailId
import me.szymanski.arch.domain.data.LoadingState
import me.szymanski.arch.domain.data.RepositoryId
import me.szymanski.arch.domain.details.DetailsLogic
import me.szymanski.arch.utils.map
import me.szymanski.arch.widgets.list.ListItemType.ListItem
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val detailsLogic: DetailsLogic
) : ViewModel() {

    val items = detailsLogic.items.map(viewModelScope) { list -> list.map { ListItem(it.type.name, it.type.toTitle(), it.value) } }
    val isListVisible = detailsLogic.loadingState.map(viewModelScope) { it == LoadingState.SUCCESS }
    val isLoading = detailsLogic.loadingState.map(viewModelScope) { it == LoadingState.LOADING }
    val title = detailsLogic.title
    val error = detailsLogic.loadingState.map(viewModelScope) {
        if (it == LoadingState.ERROR) getString(context, R.string.loading_details_error) else null
    }

    fun onBackClick() {
        detailsLogic.onBackClick()
    }

    private fun DetailId.toTitle(): String = getString(
        context,
        when (this) {
            DetailId.NAME -> R.string.detail_name
            DetailId.DESCRIPTION -> R.string.detail_description
            DetailId.PRIVATE -> R.string.detail_private
            DetailId.OWNER -> R.string.detail_owner
            DetailId.FORKS -> R.string.detail_forks
            DetailId.LANGUAGE -> R.string.detail_language
            DetailId.ISSUES -> R.string.detail_issues
            DetailId.LICENSE -> R.string.detail_license
            DetailId.WATCHERS -> R.string.detail_watchers
            DetailId.BRANCH -> R.string.detail_branch
        }
    )

    fun setRepositoryId(id: RepositoryId) {
        detailsLogic.loadDetails(viewModelScope, id)
    }
}
