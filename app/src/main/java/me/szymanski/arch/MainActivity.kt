package me.szymanski.arch

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import me.szymanski.arch.logic.cases.ListLogic
import me.szymanski.arch.utils.AndroidScreen
import me.szymanski.arch.utils.changeFragment
import me.szymanski.arch.utils.isWideScreen
import me.szymanski.arch.widgets.FrameDouble
import me.szymanski.arch.widgets.FrameDouble.State.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), AndroidScreen {
    private val viewModel: ListViewModel by viewModels()
    lateinit var view: FrameDouble
    override val ctx: Context = this
    override var disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = FrameDouble(this)
        setContentView(view)
        if (savedInstanceState == null) view.apply {
            changeFragment(leftColumn.id, ListFragment())
            changeFragment(rightColumn.id, DetailsFragment())
        }
    }

    override fun onStart() {
        super.onStart()
        linkViewAndLogic(view, viewModel.logic)
    }

    override fun onStop() {
        disposables.dispose()
        disposables = CompositeDisposable()
        super.onStop()
    }

    private fun linkViewAndLogic(view: FrameDouble, logic: ListLogic) {
        var initiated = false
        logic.wideScreen = isWideScreen()
        logic.closeApp.observeOnUi { finish() }
        logic.showViews.map {
            when (it!!) {
                ListLogic.ShowViews.LIST -> LEFT
                ListLogic.ShowViews.DETAILS -> RIGHT
                ListLogic.ShowViews.BOTH -> BOTH
            }
        }.observeChangedOnUi {
            view.setState(it, initiated)
            initiated = true
        }
    }

    override fun onBackPressed() {
        viewModel.logic.onBackPressed()
    }
}
