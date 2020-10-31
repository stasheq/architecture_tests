package me.szymanski.arch

import android.content.Context
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import me.szymanski.arch.logic.cases.MainCase
import me.szymanski.arch.widgets.FrameDouble
import me.szymanski.arch.widgets.FrameSingle
import me.szymanski.glue.GlueActivity
import me.szymanski.glue.ViewWidget

@AndroidEntryPoint
class MainActivity : GlueActivity<MainCase, ViewWidget>() {

    override fun linkViewAndLogic(view: ViewWidget, case: MainCase) {
        case.selectedRepoName.onNext {
            if (it.get() != null) when (view) {
                is FrameSingle -> changeFragment(view.frame.id, DetailsFragment())
                is FrameDouble -> changeFragment((view.rightColumn.id), DetailsFragment())
            }
        }
        case.backPressed.onNext { finish() }
        case.onDetailsBackPress.onNext {
            when (view) {
                is FrameSingle -> changeFragment(view.frame.id, ListFragment())
                is FrameDouble -> finish()
            }
        }
    }

    override fun createView(ctx: Context, parent: ViewGroup?): ViewWidget {
        return if (isWideScreen()) FrameDouble(ctx).apply {
            changeFragment(leftColumn.id, ListFragment())
            changeFragment(rightColumn.id, DetailsFragment())
        } else FrameSingle(ctx).apply {
            changeFragment(frame.id, ListFragment())
        }
    }
}
