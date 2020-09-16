package me.szymanski.glueandroid

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import me.szymanski.gluekotlin.Case

abstract class GlueFragment<T : Case> : Fragment() {
    private val model: GenericViewModel<T> by viewModels { caseFactory() }
    private var disposables = CompositeDisposable()
    lateinit var case: Case

    protected fun <T> Observable<T>.onNext(onNext: (next: T) -> Unit) = disposables.add(
        subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ next -> onNext(next) }, { error -> throw error })
    )

    override fun onStart() {
        super.onStart()
        disposables.dispose()
        disposables = CompositeDisposable()
        val case = model.case
        val parent = parentFragment
        if (parent is GlueFragment<*>) {
            case.parent = parent.case
        } else {
            case.parent = null
        }
        linkCase(case)
    }

    override fun onStop() {
        disposables.dispose()
        super.onStop()
    }

    abstract fun linkCase(case: T)
    abstract fun caseFactory(): ViewModelFactory<GenericViewModel<T>>
}
