package me.szymanski.arch

import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import me.szymanski.arch.glue.GlueActivity
import me.szymanski.arch.logic.cases.MainCase
import me.szymanski.arch.screens.MainFrame
import me.szymanski.arch.glue.GenericViewModel
import me.szymanski.arch.glue.ViewModelFactory

class MainActivity : GlueActivity<MainCase, MainFrame>() {
    private lateinit var mainFrame: MainFrame

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(mainFrame.frameId, ListFragment())
                .commitNow()
        }
    }

    override fun linkViewAndLogic(view: MainFrame, case: MainCase) {
        case.selectedRepoName.onNext {
            val transaction = supportFragmentManager.beginTransaction()
                .replace(mainFrame.frameId, DetailsFragment())
            if (!view.isPreviewVisible) transaction.addToBackStack(null)
            transaction.commit()
        }
    }

    override fun caseFactory(): ViewModelFactory<GenericViewModel<MainCase>> = component.mainVMFactory()
    override fun createView(ctx: Context, parent: ViewGroup?) = MainFrame(ctx).apply { mainFrame = this }
}
