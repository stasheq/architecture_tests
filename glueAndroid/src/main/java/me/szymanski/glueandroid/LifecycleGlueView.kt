package me.szymanski.glueandroid

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.DisposableContainer
import me.szymanski.gluekotlin.Case

interface LifecycleGlueView<T : Case> {

    fun linkCase(case: T)
    fun caseFactory(): ViewModelFactory<GenericViewModel<T>>
    val disposableContainer: DisposableContainer

    fun <T> Observable<T>.onNext(onNext: (next: T) -> Unit) = disposableContainer.add(
        subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ next -> onNext(next) }, { error -> throw error })
    )
}
