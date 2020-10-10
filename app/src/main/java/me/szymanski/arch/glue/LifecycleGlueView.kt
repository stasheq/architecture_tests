package me.szymanski.arch.glue

import android.content.Context
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.DisposableContainer
import me.szymanski.arch.ViewWidget
import me.szymanski.arch.logic.Case

interface LifecycleGlueView<C : Case, V : ViewWidget> {

    fun linkViewAndLogic(view: V, case: C)
    fun caseFactory(): ViewModelFactory<GenericViewModel<C>>
    fun createView(ctx: Context, parent: ViewGroup?): V
    val disposableContainer: DisposableContainer
    val caseKey: String?

    fun <T> Observable<T>.onNext(onNext: (next: T) -> Unit) = disposableContainer.add(
        subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ next -> onNext(next) }, { error -> throw error })
    )
}

@Suppress("UNCHECKED_CAST")
fun <C : Case, V : ViewWidget> LifecycleGlueView<C, V>.lazyCreateViewModel(getActivity: () -> FragmentActivity) = lazy {
    val className = this::class.qualifiedName
        ?: throw IllegalArgumentException("Local and anonymous classes can not be ViewModels")
    val key = if (caseKey == null) className else "$caseKey:$className"
    ViewModelProvider(getActivity(), caseFactory()).get(key, GenericViewModel::class.java) as GenericViewModel<C>
}
