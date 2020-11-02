package me.szymanski.arch.logic.cases

import com.jakewharton.rxrelay3.BehaviorRelay
import com.jakewharton.rxrelay3.PublishRelay
import io.reactivex.rxjava3.core.Observable
import me.szymanski.arch.logic.rest.RestConfig
import me.szymanski.glue.CaseTemplate
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject

interface MainLogic {
    val onDetailsBackPress: Observable<Unit>
    val backPressed: Observable<Unit>
    val selectedRepoName: Observable<AtomicReference<String?>>

    fun detailsBackPressed()

}

class MainLogicImpl @Inject constructor(restConfig: RestConfig) : CaseTemplate(), MainLogic {
    override val selectedRepoName: BehaviorRelay<AtomicReference<String?>> =
        BehaviorRelay.create<AtomicReference<String?>>().apply {
            accept(AtomicReference(null))
        }
    val userName: BehaviorRelay<String> = BehaviorRelay.create<String>().apply {
        accept(restConfig.defaultUser)
    }
    override val onDetailsBackPress: PublishRelay<Unit> = PublishRelay.create()

    init {
        enableBackPress = true
    }

    override fun detailsBackPressed() {
        selectedRepoName.accept(AtomicReference(null))
        onDetailsBackPress.accept(Unit)
    }
}
