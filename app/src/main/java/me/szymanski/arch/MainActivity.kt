package me.szymanski.arch

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import me.szymanski.arch.logic.cases.ListLogic
import me.szymanski.arch.utils.AndroidScreen
import me.szymanski.arch.widgets.FrameDouble

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
        logic.wideScreen = isWideScreen()
        logic.closeApp.observeOnUi { finish() }
        logic.showList.observeOnUi { view.leftColumn.isVisible = it }
        logic.showDetails.observeOnUi { view.rightColumn.isVisible = it }
    }

    override fun onBackPressed() {
        viewModel.logic.onBackPressed()
    }
}
