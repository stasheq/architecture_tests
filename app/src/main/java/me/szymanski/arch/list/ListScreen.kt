package me.szymanski.arch.list

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.szymanski.arch.R
import me.szymanski.arch.screens.ListScreen


@Composable
fun ListScreen(
    viewModel: ListViewModel = hiltViewModel()
) {
    viewModel.init()
    ListScreen(
        items = viewModel.items.collectAsStateWithLifecycle(),
        isListVisible = viewModel.listVisible.collectAsStateWithLifecycle(),
        centerLoading = viewModel.centerLoading.collectAsStateWithLifecycle(),
        pullLoading = viewModel.pullLoading.collectAsStateWithLifecycle(),
        hasNextPage = viewModel.hasNextPage.collectAsStateWithLifecycle(),
        error = viewModel.error.collectAsStateWithLifecycle(),
        errorIconDescription = stringResource(R.string.icon_error),
        defaultValue = viewModel.userName,
        onValueChange = viewModel::onValueChange,
        searchIconDescription = stringResource(R.string.icon_search),
        onPullToRefresh = viewModel::onPullToRefresh,
        onLoadNextPage = viewModel::loadNextPage
    )
}
