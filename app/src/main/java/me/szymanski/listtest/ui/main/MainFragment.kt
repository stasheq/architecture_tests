package me.szymanski.listtest.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import me.szymanski.listtest.Application
import me.szymanski.listtest.GenericViewModel
import me.szymanski.listtest.R
import me.szymanski.listtest.injector
import me.szymanski.logic.Logger
import me.szymanski.logic.cases.RepositoriesListCase
import javax.inject.Inject

class MainFragment : Fragment() {
    private val model: GenericViewModel<RepositoriesListCase> by activityViewModels {
        requireActivity().injector.reposListViewModelFactory()
    }

    @Inject
    lateinit var logger: Logger

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context.applicationContext as Application).component.inject(this)
    }

    override fun onStart() {
        super.onStart()
        model.case.loading.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result ->
                logger.log("$result")
            }
        model.start()
    }
}
