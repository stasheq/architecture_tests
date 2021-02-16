package me.szymanski.arch.utils

import android.content.Context
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import me.szymanski.arch.di.log

interface WithDisposables {
    var disposables: CompositeDisposable
}

interface WithContext {
    val ctx: Context
}

interface AndroidScreen : WithContext, WithDisposables {
    fun makeCoroutineScope() = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    fun <T> Flow<T>.observeOnUi(onNext: (next: T) -> Unit) = asObservable().observeOnUi(onNext)

    fun <T> Flow<T>.observeChangedOnUi(onNext: (next: T) -> Unit) = asObservable().observeChangedOnUi(onNext)

    fun <T> Flow<T>.asObservable(): Observable<T> = Observable.create { emitter ->
        val scope = makeCoroutineScope()
        emitter.setCancellable { scope.cancel() }
        scope.launch { collect { if (!emitter.isDisposed) emitter.onNext(it) } }
        catch { if (!emitter.isDisposed) emitter.onError(it) }
    }

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
