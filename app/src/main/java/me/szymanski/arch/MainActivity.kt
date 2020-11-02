package me.szymanski.arch

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import me.szymanski.arch.logic.cases.MainLogic
import me.szymanski.arch.utils.observeOnUi
import me.szymanski.arch.widgets.FrameDouble
import me.szymanski.arch.widgets.FrameSingle
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var logic: MainLogic
    lateinit var view: ViewWidget
    private var disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = if (isWideScreen()) FrameDouble(this).apply {
            changeFragment(leftColumn.id, ListFragment())
            changeFragment(rightColumn.id, DetailsFragment())
        } else FrameSingle(this).apply {
            changeFragment(frame.id, ListFragment())
        }
        setContentView(view)
    }

    override fun onStart() {
        super.onStart()
        linkViewAndLogic(view, logic)
    }

    override fun onStop() {
        disposables.dispose()
        disposables = CompositeDisposable()
        super.onStop()
    }

    private fun linkViewAndLogic(view: ViewWidget, case: MainLogic) {
        case.selectedRepoName.observeOnUi(disposables) {
            if (it.get() != null) when (view) {
                is FrameSingle -> changeFragment(view.frame.id, DetailsFragment())
                is FrameDouble -> changeFragment((view.rightColumn.id), DetailsFragment())
            }
        }
        case.backPressed.observeOnUi(disposables) { finish() }
        case.onDetailsBackPress.observeOnUi(disposables) {
            when (view) {
                is FrameSingle -> changeFragment(view.frame.id, ListFragment())
                is FrameDouble -> finish()
            }
        }
    }

    @SuppressLint("MissingSuperCall") // to not store fragments state
    override fun onSaveInstanceState(outState: Bundle) = Unit
}
