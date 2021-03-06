package me.szymanski.arch.utils

import android.content.Context
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import me.szymanski.arch.di.log

interface WithDisposables {
    var disposables: CompositeDisposable
}

interface WithContext {
    val ctx: Context
}

interface AndroidScreen : WithContext, WithDisposables {
    fun <T> Observable<T>.observeOnUi(onNext: (next: T) -> Unit) =
        disposables.add(internalObserveOnUi(onNext))

    fun <T> Observable<T>.observeChangedOnUi(onNext: (next: T) -> Unit) =
        disposables.add(distinctUntilChanged().internalObserveOnUi(onNext))

    private fun <T> Observable<T>.internalObserveOnUi(onNext: (next: T) -> Unit): Disposable =
        subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ next ->
                ctx.log("Received: $next")
                onNext(next)
            }, { error -> throw error })
}
