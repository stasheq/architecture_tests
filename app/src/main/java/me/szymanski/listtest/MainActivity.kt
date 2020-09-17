package me.szymanski.listtest

import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import me.szymanski.glueandroid.GenericViewModel
import me.szymanski.glueandroid.GlueActivity
import me.szymanski.glueandroid.ViewModelFactory
import me.szymanski.listtest.ui.list.ListFragment
import me.szymanski.logic.cases.MainCase
import me.szymanski.screens.MainFrame

class MainActivity : GlueActivity<MainCase, MainFrame>() {
    private lateinit var mainFrame: MainFrame

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(mainFrame.frameId, ListFragment())
                .commitNow()
        }
    }

    override fun linkViewAndLogic(view: MainFrame, case: MainCase) = Unit
    override fun caseFactory(): ViewModelFactory<GenericViewModel<MainCase>> = component.mainVMFactory()
    override fun createView(ctx: Context, parent: ViewGroup?): MainFrame {
        mainFrame = MainFrame(ctx)
        return mainFrame
    }
}
