package me.szymanski.arch.logic.cases

import com.jakewharton.rxrelay3.BehaviorRelay
import com.jakewharton.rxrelay3.PublishRelay
import me.szymanski.arch.logic.rest.RestConfig
import me.szymanski.glue.BaseCase
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject

class MainCase @Inject constructor(restConfig: RestConfig) : BaseCase() {
    val selectedRepoName: BehaviorRelay<AtomicReference<String?>> =
        BehaviorRelay.create<AtomicReference<String?>>().apply {
            accept(AtomicReference(null))
        }
    val userName: BehaviorRelay<String> = BehaviorRelay.create<String>().apply {
        accept(restConfig.defaultUser)
    }
    val onDetailsBackPress: PublishRelay<Unit> = PublishRelay.create()

    init {
        enableBackPress = true
    }

    fun detailsBackPressed() {
        selectedRepoName.accept(AtomicReference(null))
        onDetailsBackPress.accept(Unit)
    }
}
