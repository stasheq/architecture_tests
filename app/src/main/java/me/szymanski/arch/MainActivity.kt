package me.szymanski.arch

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import me.szymanski.arch.logic.screenslogic.ListLogic
import me.szymanski.arch.utils.changeFragment
import me.szymanski.arch.utils.isWideScreen
import me.szymanski.arch.widgets.FrameDouble
import me.szymanski.arch.widgets.FrameDouble.State.BOTH
import me.szymanski.arch.widgets.FrameDouble.State.LEFT
import me.szymanski.arch.widgets.FrameDouble.State.RIGHT

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: ListViewModel by viewModels()
    lateinit var view: FrameDouble

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = FrameDouble(this)
        setContentView(view)
        if (savedInstanceState == null) view.apply {
            changeFragment(leftColumn.id, ListFragment())
            changeFragment(rightColumn.id, DetailsFragment())
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                subscribeToLogic(view, viewModel.logic)
            }
        }
    }

    private suspend fun subscribeToLogic(view: FrameDouble, logic: ListLogic) = coroutineScope {
        var initiated = false
        logic.wideScreen = isWideScreen()
        launch { logic.closeApp.collect { finish() } }
        launch {
            logic.showViews.map {
                when (it) {
                    ListLogic.ShowViews.LIST -> LEFT
                    ListLogic.ShowViews.DETAILS -> RIGHT
                    ListLogic.ShowViews.BOTH -> BOTH
                }
            }.collect {
                view.setState(it, initiated)
                initiated = true
            }
        }
    }

    override fun onBackPressed() {
        viewModel.logic.onBackPressed()
    }
}
