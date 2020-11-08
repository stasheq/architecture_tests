package me.szymanski.arch

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.hilt.lifecycle.ViewModelInject
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import me.szymanski.arch.di.LogicViewModel
import me.szymanski.arch.screens.RepositoryDetails
import me.szymanski.arch.logic.cases.DetailsLogic.LoadingState.LOADING
import me.szymanski.arch.logic.cases.DetailsLogic.LoadingState.ERROR
import me.szymanski.arch.logic.cases.DetailsLogic.LoadingState.SUCCESS
import me.szymanski.arch.logic.cases.DetailsLogic
import me.szymanski.arch.logic.cases.DetailsLogicImpl
import me.szymanski.arch.utils.AndroidScreen

class DetailsViewModel @ViewModelInject constructor(logic: DetailsLogicImpl) : LogicViewModel<DetailsLogic>(logic)

@AndroidEntryPoint
class DetailsFragment : Fragment(), AndroidScreen {
    private val viewModel: DetailsViewModel by activityViewModels()
    private lateinit var view: RepositoryDetails
    override var disposables = CompositeDisposable()
    override val ctx: Context by lazy { requireContext() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view = RepositoryDetails(inflater.context, container)
        return view.root
    }

    override fun onStart() {
        super.onStart()
        linkViewAndLogic(view, viewModel.logic)
    }

    override fun onStop() {
        disposables.dispose()
        disposables = CompositeDisposable()
        super.onStop()
    }

    private fun linkViewAndLogic(view: RepositoryDetails, case: DetailsLogic) {
        case.state.observeOnUi { state ->
            view.loading = state == LOADING
            view.errorText = if (state == ERROR) getString(R.string.loading_details_error) else null
            view.detailsVisible = state == SUCCESS
        }
        case.result.observeOnUi { view.title = it.name }
        case.reload()
    }
}
