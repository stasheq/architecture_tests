package me.szymanski.arch.glue

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.reactivex.rxjava3.disposables.CompositeDisposable
import me.szymanski.arch.ViewWidget
import me.szymanski.arch.logic.Case

abstract class GlueFragment<C : Case, V : ViewWidget> : Fragment(), LifecycleGlueView<C, V> {
    private val model: GenericViewModel<C> by lazyCreateViewModel(::requireActivity)
    override var disposableContainer = CompositeDisposable()
    internal lateinit var logic: C
    private lateinit var view: V
    override val caseKey: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view = createView(inflater.context, container)
        return view.root
    }

    override fun onStart() {
        super.onStart()
        disposableContainer.dispose()
        disposableContainer = CompositeDisposable()
        logic = model.case
        when (val parent = parentFragment ?: activity) {
            is GlueFragment<*, *> -> logic.parent = parent.logic
            is GlueActivity<*, *> -> logic.parent = parent.logic
            else -> logic.parent = null
        }
        linkViewAndLogic(view, logic)
    }

    override fun onStop() {
        disposableContainer.dispose()
        super.onStop()
    }
}
