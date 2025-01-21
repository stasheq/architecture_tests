package me.szymanski.arch.list

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import me.szymanski.arch.R
import me.szymanski.arch.domain.data.Repository
import me.szymanski.arch.domain.list.ListLogic
import me.szymanski.arch.domain.list.data.ErrorType
import me.szymanski.arch.utils.combine
import me.szymanski.arch.utils.map
import me.szymanski.arch.designlib.listitem.ListItemType.ListItem
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val listLogic: ListLogic
) : ViewModel() {
    private var afterPullToRefresh = false

    val userName
        get() = listLogic.userName
    val centerLoading = listLogic.loading.map(viewModelScope) {
        it && !afterPullToRefresh
    }
    val pullLoading = listLogic.loading.map(viewModelScope) {
        it && afterPullToRefresh
    }
    val error = listLogic.error.map(viewModelScope) {
        when (it) {
            ErrorType.USER_DOESNT_EXIST -> context.getString(R.string.loading_error_doesnt_exist)
            ErrorType.NO_CONNECTION, ErrorType.OTHER -> context.getString(R.string.loading_error_other)
            null -> null
        }
    }
    val items = listLogic.list.map(viewModelScope) {
        it?.mapToUI() ?: emptyList()
    }
    val listVisible = combine(centerLoading, error, viewModelScope) { loading, error ->
        error == null && !loading
    }

    val hasNextPage = listLogic.hasNextPage

    fun init() {
        reload()
    }

    fun onPullToRefresh() {
        afterPullToRefresh = true
        reload()
    }

    fun reload() = listLogic.reload(viewModelScope)

    fun loadNextPage() = listLogic.loadNextPage(viewModelScope)

    private fun List<Repository>.mapToUI() = map {
        ListItem(it.name, it.name, it.description) { listLogic.itemClick(it) }
    }

    fun onValueChange(value: String) {
        listLogic.onUserNameInput(viewModelScope, value)
    }
}
