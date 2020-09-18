package me.szymanski.arch

import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
            changeFragment(mainFrame.frameId, ListFragment())
        }
    }

    override fun linkViewAndLogic(view: MainFrame, case: MainCase) {
        case.selectedRepoName.onNext {
            changeFragment(mainFrame.frameId, DetailsFragment(), true, "DetailsFragment")
        }
    }

    private fun changeFragment(frameId: Int, fragment: Fragment, addToBackStack: Boolean = false, tag: String? = null) {
        if (tag != null && supportFragmentManager.findFragmentByTag(tag) != null) return
        val transaction = supportFragmentManager.beginTransaction()
            .replace(frameId, fragment, tag)
        if (addToBackStack) transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun caseFactory(): ViewModelFactory<GenericViewModel<MainCase>> = component.mainVMFactory()
    override fun createView(ctx: Context, parent: ViewGroup?) = MainFrame(ctx).apply { mainFrame = this }
}
