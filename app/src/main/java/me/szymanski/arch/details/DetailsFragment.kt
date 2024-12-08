package me.szymanski.arch.details

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.parcelize.Parcelize
import me.szymanski.arch.domain.data.RepositoryId
import me.szymanski.arch.domain.details.DetailsLogic
import me.szymanski.arch.screens.DetailsScreen
import me.szymanski.arch.utils.fragmentArgs
import javax.inject.Inject

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    @Inject
    lateinit var logic: DetailsLogic
    private var args by fragmentArgs<Args>()

    override fun setArguments(args: Bundle?) {
        super.setArguments(args)
        if (::logic.isInitialized) logic.repositoryId = this.args.repositoryId
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logic.repositoryId = args.repositoryId
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        ComposeView(inflater.context).apply {
            setContent { DetailsScreenComposable() }
        }

    @Composable
    fun DetailsScreenComposable(
        viewModel: DetailsViewModel = hiltViewModel()
    ) = DetailsScreen(
        title = viewModel.title.collectAsStateWithLifecycle(),
        items = viewModel.items.collectAsStateWithLifecycle(),
        isListVisible = viewModel.isListVisible.collectAsStateWithLifecycle(),
        isLoading = viewModel.isLoading.collectAsStateWithLifecycle(),
        error = viewModel.error.collectAsStateWithLifecycle(),
        onBackClick = viewModel::onBackClick
    )

    companion object {
        @Parcelize
        private data class Args(val repositoryId: RepositoryId?) : Parcelable

        fun instantiate(repositoryId: RepositoryId?) = DetailsFragment().apply { args = Args(repositoryId) }
    }
}
