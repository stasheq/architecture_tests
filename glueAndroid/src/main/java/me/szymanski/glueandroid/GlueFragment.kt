package me.szymanski.glueandroid

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import io.reactivex.rxjava3.disposables.CompositeDisposable
import me.szymanski.gluekotlin.Case

abstract class GlueFragment<T : Case> : Fragment(), LifecycleGlueView<T> {
    private val model: GenericViewModel<T> by viewModels { caseFactory() }
    override var disposableContainer = CompositeDisposable()
    internal lateinit var case: T

    override fun onStart() {
        super.onStart()
        disposableContainer.dispose()
        disposableContainer = CompositeDisposable()
        case = model.case
        when (val parent = parentFragment ?: activity) {
            is GlueFragment<*> -> case.parent = parent.case
            is GlueActivity<*> -> case.parent = parent.case
            else -> case.parent = null
        }
        linkCase(case)
    }

    override fun onStop() {
        disposableContainer.dispose()
        super.onStop()
    }
}
