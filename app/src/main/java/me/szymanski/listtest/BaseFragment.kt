package me.szymanski.listtest

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import me.szymanski.logic.Case
import me.szymanski.logic.Logger
import javax.inject.Inject

open class BaseFragment<T : Case> : Fragment() {
    private val model: GenericViewModel<T> by activityViewModels {
        requireActivity().injector.reposListViewModelFactory()
    }

    @Inject
    lateinit var logger: Logger
    fun log(message: String, tag: String? = null, level: Logger.Level = Logger.Level.DEBUG) =
        logger.log(message, tag, level)

    val case: T by lazy {
        model.case
    }

    val Context.component get() = (applicationContext as Application).component

    fun <T> Observable<T>.onNext(onNext: (next: T) -> Unit) {
        subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { next -> onNext(next) }
    }

    override fun onStart() {
        super.onStart()
        model.start()
    }
}
