package me.szymanski.arch

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import me.szymanski.arch.domain.data.DetailId
import me.szymanski.arch.domain.data.LoadingState
import me.szymanski.arch.domain.data.RepositoryId
import me.szymanski.arch.domain.details.DetailsLogic
import me.szymanski.arch.screens.DetailsScreen
import me.szymanski.arch.utils.fragmentArgs
import me.szymanski.arch.utils.isWideScreen
import me.szymanski.arch.widgets.list.ListItemType.ListItem

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    @Inject
    lateinit var logic: DetailsLogic
    private var args by fragmentArgs<Args>()
    private var viewUpdateJob: Job? = null

    override fun setArguments(args: Bundle?) {
        super.setArguments(args)
        if (::logic.isInitialized) logic.repositoryId = this.args.repositoryId
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        DetailsScreen(inflater.context, container).apply {
            viewUpdateJob = lifecycleScope.launch { subscribeToLogic(this@apply, logic) }
        }.root

    override fun onDestroyView() {
        viewUpdateJob?.cancel()
        viewUpdateJob = null
        super.onDestroyView()
    }

    private fun CoroutineScope.subscribeToLogic(view: DetailsScreen, logic: DetailsLogic) {
        view.showBackIcon = !isWideScreen()
        view.backClick = { logic.onBackPressed() }
        logic.repositoryId = args.repositoryId

        launch {
            logic.state.collect { state ->
                view.loading = state == LoadingState.LOADING
                view.errorText = if (state == LoadingState.ERROR) getString(R.string.loading_details_error) else null
                view.listVisible = state == LoadingState.SUCCESS
            }
        }
        launch {
            logic.result.collect { list ->
                view.items = list.map { ListItem(it.type.name, it.type.toTitle(), it.value) }
            }
        }
        launch { logic.title.collect { view.title = it } }
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
