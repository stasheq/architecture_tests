package me.szymanski.arch.logic.cases

import com.jakewharton.rxrelay3.BehaviorRelay
import com.jakewharton.rxrelay3.PublishRelay
import me.szymanski.glue.BaseCase
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject

class MainCase @Inject constructor() : BaseCase() {
    val selectedRepoName = BehaviorRelay.create<AtomicReference<String?>>().apply {
        accept(AtomicReference(null))
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
