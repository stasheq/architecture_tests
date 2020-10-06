package me.szymanski.arch

import android.content.Context
import android.view.ViewGroup
import me.szymanski.arch.glue.GlueActivity
import me.szymanski.arch.logic.cases.MainCase
import me.szymanski.arch.widgets.FrameDouble
import me.szymanski.arch.glue.GenericViewModel
import me.szymanski.arch.glue.ViewModelFactory
import me.szymanski.arch.widgets.FrameSingle

class MainActivity : GlueActivity<MainCase, ViewWidget>() {

    override fun linkViewAndLogic(view: ViewWidget, case: MainCase) {
        case.selectedRepoName.onNext {
            when (view) {
                is FrameSingle -> changeFragment(view.frame.id, DetailsFragment(), true)
                is FrameDouble -> changeFragment((view.rightColumn.id), DetailsFragment())
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

    override fun caseFactory(): ViewModelFactory<GenericViewModel<MainCase>> = component.mainVMFactory()
}
