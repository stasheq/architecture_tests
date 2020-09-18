package me.szymanski.arch.glue

import android.content.Context
import android.view.ViewGroup
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

    fun <T> Observable<T>.onNext(onNext: (next: T) -> Unit) = disposableContainer.add(
        subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ next -> onNext(next) }, { error -> throw error })
    )
}
