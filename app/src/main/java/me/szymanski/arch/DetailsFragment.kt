package me.szymanski.arch

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.parcelize.Parcelize
import me.szymanski.arch.domain.data.DetailId
import me.szymanski.arch.domain.data.LoadingState
import me.szymanski.arch.domain.data.RepositoryId
import me.szymanski.arch.domain.details.DetailsLogic
import me.szymanski.arch.screens.DetailsScreen
import me.szymanski.arch.utils.fragmentArgs
import me.szymanski.arch.utils.isWideScreen
import me.szymanski.arch.widgets.list.ListItemType.ListItem
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
    fun DetailsScreenComposable() {
        val loadingState by logic.loadingState.collectAsStateWithLifecycle()
        val itemsState by logic.result.collectAsStateWithLifecycle()
        val titleState by logic.title.collectAsStateWithLifecycle()

        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                DetailsScreen(context).apply {
                    showBackIcon = !context.isWideScreen()
                    backClick = { logic.onBackPressed() }
                }.root
            },
            update = { view ->
                DetailsScreen(view).apply {
                    loading = loadingState == LoadingState.LOADING
                    errorText = if (loadingState == LoadingState.ERROR) getString(R.string.loading_details_error) else null
                    listVisible = loadingState == LoadingState.SUCCESS
                    items = itemsState.map { ListItem(it.type.name, it.type.toTitle(), it.value) }
                    title = titleState
                }
            }
        )
    }

    private fun DetailId.toTitle(): String = getString(
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

    companion object {
        @Parcelize
        private data class Args(val repositoryId: RepositoryId?) : Parcelable

        fun instantiate(repositoryId: RepositoryId?) = DetailsFragment().apply { args = Args(repositoryId) }
    }
}
