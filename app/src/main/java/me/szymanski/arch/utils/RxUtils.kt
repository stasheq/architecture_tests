package me.szymanski.arch.utils

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable

fun <T> Observable<T>.observeOnUi(disposables: CompositeDisposable, onNext: (next: T) -> Unit) = disposables.add(
    subscribeOn(AndroidSchedulers.mainThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({ next -> onNext(next) }, { error -> throw error })
)
