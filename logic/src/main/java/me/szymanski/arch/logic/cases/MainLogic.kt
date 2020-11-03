package me.szymanski.arch.logic.cases

import com.jakewharton.rxrelay3.BehaviorRelay
import com.jakewharton.rxrelay3.PublishRelay
import io.reactivex.rxjava3.core.Observable
import me.szymanski.arch.logic.Logic
import javax.inject.Inject

interface MainLogic : Logic {
    var wideScreen: Boolean
    val closeApp: Observable<Unit>
    val showList: Observable<Boolean>
    val showDetails: Observable<Boolean>
}

class MainLogicImpl @Inject constructor() : MainLogic {
    override var wideScreen: Boolean = false
        set(value) {
            field = value
            showList.accept(true)
            showDetails.accept(value)
        }
    override val closeApp: PublishRelay<Unit> = PublishRelay.create<Unit>()
    override val showList: BehaviorRelay<Boolean> = BehaviorRelay.create<Boolean>()
    override val showDetails: BehaviorRelay<Boolean> = BehaviorRelay.create<Boolean>()

    override fun onBackPressed(): Boolean {
        if (wideScreen || showList.value == true) {
            closeApp.accept(Unit)
        } else {
            showList.accept(true)
            showDetails.accept(false)
        }
        return true
    }
}
