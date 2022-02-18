package me.szymanski.arch

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenStarted
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import me.szymanski.arch.logic.details.DetailId
import me.szymanski.arch.logic.details.DetailsLogic
import me.szymanski.arch.logic.details.DetailsLogic.LoadingState.*
import me.szymanski.arch.logic.details.RepositoryId
import me.szymanski.arch.screens.DetailsScreen
import me.szymanski.arch.utils.fragmentArgs
import me.szymanski.arch.utils.isWideScreen
import me.szymanski.arch.widgets.list.ListItemType.ListItem

@AndroidEntryPoint
class DetailsFragment : Fragment() {
    private var args by fragmentArgs<Args>()

    @Inject
    lateinit var logic: DetailsLogic
    private lateinit var view: DetailsScreen

    override fun setArguments(args: Bundle?) {
        super.setArguments(args)
        if (::logic.isInitialized) logic.repositoryId = this.args.repositoryId
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        view = DetailsScreen(inflater.context, container)
        return view.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            whenStarted { subscribeToLogic(view, logic) }
        }
    }

    private fun CoroutineScope.subscribeToLogic(view: DetailsScreen, logic: DetailsLogic) {
        view.showBackIcon = !isWideScreen()
        view.backClick = { logic.onBackPressed() }
        logic.repositoryId = args.repositoryId

        launch {
            logic.state.collect { state ->
                view.loading = state == LOADING
                view.errorText = if (state == ERROR) getString(R.string.loading_details_error) else null
                view.listVisible = state == SUCCESS
            }
        }
        launch {
            logic.result.collect { list ->
                view.items = list.map { ListItem(it.type.name, it.type.toTitle(), it.value, it) }
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
