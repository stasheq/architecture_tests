package me.szymanski.arch.details

import android.os.Bundle
import android.os.Parcelable
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
import kotlinx.parcelize.Parcelize
import me.szymanski.arch.R
import me.szymanski.arch.domain.data.RepositoryId
import me.szymanski.arch.screens.DetailsScreen
import me.szymanski.arch.utils.fragmentArgs

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private var args by fragmentArgs<Args>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        ComposeView(inflater.context).apply {
            setContent { DetailsScreenComposable(args.repositoryId) }
        }

    companion object {
        @Parcelize
        private data class Args(val repositoryId: RepositoryId) : Parcelable

        fun instantiate(repositoryId: RepositoryId) = DetailsFragment().apply { args = Args(repositoryId) }
    }
}

@Composable
fun DetailsScreenComposable(
    repositoryId: RepositoryId,
    viewModel: DetailsViewModel = hiltViewModel()
) {
    viewModel.setRepositoryId(repositoryId)
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
