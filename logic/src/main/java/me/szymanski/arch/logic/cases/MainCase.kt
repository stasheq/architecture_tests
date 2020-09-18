package me.szymanski.arch.logic.cases

import com.jakewharton.rxrelay3.BehaviorRelay
import me.szymanski.arch.logic.BaseCase
import javax.inject.Inject

class MainCase @Inject constructor() : BaseCase() {
    val selectedRepoName: BehaviorRelay<String> = BehaviorRelay.create<String>()
}
