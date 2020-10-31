package me.szymanski.arch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import me.szymanski.arch.di.CaseReference
import me.szymanski.arch.logic.cases.DetailsCase
import me.szymanski.arch.screens.RepositoryDetails
import me.szymanski.arch.logic.cases.DetailsCase.LoadingState.LOADING
import me.szymanski.arch.logic.cases.DetailsCase.LoadingState.ERROR
import me.szymanski.arch.logic.cases.DetailsCase.LoadingState.SUCCESS
import me.szymanski.arch.utils.observeOnUi
import javax.inject.Inject

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    @Inject
    lateinit var caseRef: CaseReference<DetailsCase>
    private lateinit var view: RepositoryDetails
    private val disposables = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view = RepositoryDetails(inflater.context, container)
        linkViewAndLogic(view, caseRef.case)
        return view.root
    }

    private fun linkViewAndLogic(view: RepositoryDetails, case: DetailsCase) {
        case.loading.observeOnUi(disposables) { loadingState ->
            view.loading = loadingState == LOADING
            view.errorText = if (loadingState == ERROR) getString(R.string.error) else null
            view.detailsVisible = loadingState == SUCCESS
        }
        case.result.observeOnUi(disposables) { view.title = it.name }
        case.reload()
    }

    override fun onDestroyView() {
        disposables.dispose()
        super.onDestroyView()
    }
}
