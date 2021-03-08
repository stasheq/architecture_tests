package me.szymanski.arch.screens

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import me.szymanski.arch.getValue
import me.szymanski.arch.inflate
import me.szymanski.arch.setValue
import me.szymanski.arch.widgets.ErrorBar
import me.szymanski.arch.widgets.Toolbar
import me.szymanski.arch.widgets.databinding.ScreenRepoDetailsBinding
import me.szymanski.arch.widgets.list.ListWidget

class DetailsScreen(ctx: Context, parent: ViewGroup? = null) {
    private val errorBar: ErrorBar
    private val loadingView: View
    private val toolbar: Toolbar
    private val list: ListWidget

    val root = ctx.inflate(ScreenRepoDetailsBinding::inflate, parent).apply {
        loadingView = detailsLoading
        toolbar = detailsToolbar
        toolbar.showBackIcon = true
        errorBar = detailsErrorBar
        list = detailsList
        list.refreshingEnabled = false
        list.selectingEnabled = false
    }.root

    var errorText by errorBar::errorText
    var loading by loadingView::isVisible
    var title by toolbar::title
    var backClick by toolbar::backClick
    var showBackIcon by toolbar::showBackIcon
    var items by list::items
    var listVisible by list::isVisible
}
