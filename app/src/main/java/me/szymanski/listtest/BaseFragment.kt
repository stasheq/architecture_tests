package me.szymanski.listtest

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import me.szymanski.logic.Case
import me.szymanski.logic.Logger
import javax.inject.Inject

abstract class BaseFragment<T : Case> : Fragment() {
    private val model: GenericViewModel<T> by viewModels { injector.reposListViewModelFactory() }
    private var disposables = CompositeDisposable()

    @Inject
    lateinit var logger: Logger
    fun log(message: String, tag: String? = null, level: Logger.Level = Logger.Level.DEBUG) =
        logger.log(message, tag, level)

    fun <T> Observable<T>.onNext(onNext: (next: T) -> Unit) = disposables.add(
        subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { next -> onNext(next) }
    )

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        disposables.dispose()
        disposables = CompositeDisposable()
        linkCase(model.case)
    }

    override fun onDestroyView() {
        disposables.dispose()
        super.onDestroyView()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject((context.applicationContext as Application).component)
    }

    abstract fun linkCase(case: T)
    abstract fun inject(component: ApplicationComponent)
}
