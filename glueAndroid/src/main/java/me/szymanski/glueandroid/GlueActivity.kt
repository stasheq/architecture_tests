package me.szymanski.glueandroid

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.rxjava3.disposables.CompositeDisposable
import me.szymanski.gluekotlin.Case

abstract class GlueActivity<T : Case> : AppCompatActivity(), LifecycleGlueView<T> {
    private val model: GenericViewModel<T> by viewModels { caseFactory() }
    override var disposableContainer = CompositeDisposable()
    internal lateinit var case: T

    override fun onStart() {
        super.onStart()
        disposableContainer.dispose()
        disposableContainer = CompositeDisposable()
        case = model.case
        case.parent = null
        linkCase(case)
    }

    override fun onStop() {
        disposableContainer.dispose()
        super.onStop()
    }

}