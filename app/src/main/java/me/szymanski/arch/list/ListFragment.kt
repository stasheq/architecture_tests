package me.szymanski.arch.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import me.szymanski.arch.R
import me.szymanski.arch.screens.ListScreen

@AndroidEntryPoint
class ListFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        ComposeView(inflater.context).apply {
            setContent { ListScreenComposable() }
        }

    companion object {
        fun instantiate() = ListFragment()
    }
}

@Composable
fun ListScreenComposable(
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
