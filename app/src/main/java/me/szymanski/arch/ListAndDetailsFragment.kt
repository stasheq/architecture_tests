package me.szymanski.arch

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.parcelize.Parcelize
import me.szymanski.arch.logic.details.RepositoryId
import me.szymanski.arch.screens.ColumnsScreen
import me.szymanski.arch.utils.changeFragment
import me.szymanski.arch.utils.fragmentArgs

@AndroidEntryPoint
class ListAndDetailsFragment : Fragment() {
    private var args by fragmentArgs<Args>()

    private lateinit var view: ColumnsScreen

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ColumnsScreen(inflater.context, container).apply { view = this }.root

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        changeFragment(ListFragment.instantiate(), view.leftColumnId)
        changeFragment(DetailsFragment.instantiate(args.repositoryId), view.rightColumnId)
    }

    companion object {
        @Parcelize
        private data class Args(val repositoryId: RepositoryId?) : Parcelable

        fun instantiate(repositoryId: RepositoryId?) = ListAndDetailsFragment().apply { args = Args(repositoryId) }
    }
}
