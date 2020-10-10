package me.szymanski.arch.glue

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.rxjava3.disposables.CompositeDisposable
import me.szymanski.arch.ViewWidget
import me.szymanski.arch.logic.Case

abstract class GlueActivity<C : Case, V : ViewWidget> : AppCompatActivity(), LifecycleGlueView<C, V> {
    private val model: GenericViewModel<C> by lazyCreateViewModel { this }
    override var disposableContainer = CompositeDisposable()
    internal lateinit var logic: C
    private lateinit var view: V
    private val keyState = "state"
    override val caseKey: String? = null

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

    @SuppressLint("MissingSuperCall") // on purpose, data should be preserved in case
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(keyState, model.case.onSaveState())
    }
}
