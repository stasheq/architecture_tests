package me.szymanski.arch

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.hilt.lifecycle.ViewModelInject
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import me.szymanski.arch.di.LogicViewModel
import me.szymanski.arch.logic.cases.MainLogic
import me.szymanski.arch.logic.cases.MainLogicImpl
import me.szymanski.arch.utils.observeOnUi
import me.szymanski.arch.widgets.FrameDouble

class MainViewModel @ViewModelInject constructor(logic: MainLogicImpl) : LogicViewModel<MainLogic>(logic)

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()
    lateinit var view: FrameDouble
    private var disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = FrameDouble(this)
        setContentView(view)
        updateColumns(showLeft = true, showRight = isWideScreen())

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

    private fun linkViewAndLogic(view: FrameDouble, case: MainLogic) {
        case.selectedRepoName.filter { !it.get().isNullOrBlank() }.observeOnUi(disposables) {
            changeFragment((view.rightColumn.id), DetailsFragment())
            if (!isWideScreen()) updateColumns(showLeft = false, showRight = true)
        }
        case.backPressed.observeOnUi(disposables) { finish() }
        case.onDetailsBackPress.observeOnUi(disposables) {
            if (isWideScreen()) finish() else updateColumns(showLeft = true, showRight = false)
        }
    }

    private fun updateColumns(showLeft: Boolean, showRight: Boolean) {
        view.leftColumn.isVisible = showLeft
        view.rightColumn.isVisible = showRight
    }
}
