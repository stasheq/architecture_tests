package me.szymanski.arch.details

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.szymanski.arch.R
import me.szymanski.arch.domain.navigation.NavigationScreen
import me.szymanski.arch.screens.DetailsScreen

@Composable
fun DetailsScreen(
    args: NavigationScreen.Details,
    viewModel: DetailsViewModel = hiltViewModel()
) {
    viewModel.setArgs(args)
    DetailsScreen(
        title = viewModel.title.collectAsStateWithLifecycle(),
        items = viewModel.items.collectAsStateWithLifecycle(),
        isListVisible = viewModel.isListVisible.collectAsStateWithLifecycle(),
        isLoading = viewModel.isLoading.collectAsStateWithLifecycle(),
        error = viewModel.error.collectAsStateWithLifecycle(),
        errorIconDescription = stringResource(R.string.icon_error),
        onBackClick = viewModel::onBackClick,
        onBackIconDescription = stringResource(R.string.icon_back),
    )
}
