package me.szymanski.arch

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
import me.szymanski.arch.utils.observeOnUi

class DetailsViewModel @ViewModelInject constructor(logic: DetailsLogicImpl) : LogicViewModel<DetailsLogic>(logic)

@AndroidEntryPoint
class DetailsFragment : Fragment() {
    private val viewModel: DetailsViewModel by activityViewModels()
    private lateinit var view: RepositoryDetails
    private var disposables = CompositeDisposable()

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
        case.state.observeOnUi(disposables) { state ->
            view.loading = state == LOADING
            view.errorText = if (state == ERROR) getString(R.string.error) else null
            view.detailsVisible = state == SUCCESS
        }
        case.result.observeOnUi(disposables) { view.title = it.name }
        case.reload()
    }
}
