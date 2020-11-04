package me.szymanski.arch.utils

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

fun <T> Observable<T>.observeOnUi(disposables: CompositeDisposable, onNext: (next: T) -> Unit) = disposables.add(
    subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({ next -> onNext(next) }, { error -> throw error })
)
