package me.szymanski.arch.glue

import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.rxjava3.disposables.CompositeDisposable
import me.szymanski.arch.ViewWidget
import me.szymanski.arch.logic.Case

abstract class GlueActivity<C : Case, V : ViewWidget> : AppCompatActivity(), LifecycleGlueView<C, V> {
    private val model: GenericViewModel<C> by viewModels { caseFactory() }
    override var disposableContainer = CompositeDisposable()
    internal lateinit var logic: C
    private lateinit var view: V

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = createView(this, null)
        setContentView(
            view.root,
            ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        )
        logic = model.case
        logic.parent = null
    }

    override fun onStart() {
        super.onStart()
        disposableContainer.dispose()
        disposableContainer = CompositeDisposable()
        linkViewAndLogic(view, logic)
    }

    override fun onStop() {
        disposableContainer.dispose()
        super.onStop()
    }
}