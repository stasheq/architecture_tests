package me.szymanski.arch.screens

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import me.szymanski.arch.ViewWidget
import me.szymanski.arch.getValue
import me.szymanski.arch.setValue
import me.szymanski.arch.widgets.ErrorBar
import me.szymanski.arch.widgets.Toolbar
import me.szymanski.arch.widgets.databinding.ScreenRepoDetailsBinding
import me.szymanski.arch.widgets.list.ListWidget

class DetailsScreen(ctx: Context, parent: ViewGroup? = null) : ViewWidget {
    private val errorWidget: ErrorBar
    private val loadingView: View
    private val toolbar: Toolbar
    private val list: ListWidget

    override val root = ScreenRepoDetailsBinding.inflate(
        LayoutInflater.from(ctx), parent, false
    ).apply {
        loadingView = detailsLoading
        toolbar = Toolbar(ctx, this.root)
        toolbar.showBackIcon = true
        detailsList.addView(toolbar.root)
        errorWidget = ErrorBar(ctx, this.root)
        detailsList.addView(errorWidget.root)
        list = ListWidget(ctx, root)
        list.refreshingEnabled = false
        list.selectingEnabled = false
        detailsList.addView(list.root)
    }.root

    var errorText by errorWidget::errorText
    var loading by loadingView::isVisible
    var title by toolbar::title
    var backClick by toolbar::backClick
    var showBackIcon by toolbar::showBackIcon
    var items by list::items
    var listVisible by list.root::isVisible
}
