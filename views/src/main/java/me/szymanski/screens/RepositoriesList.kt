package me.szymanski.screens

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import com.example.widgets.R
import me.szymanski.glueandroid.ViewWidget
import kotlinx.android.synthetic.main.screen_repos_list.view.*
import me.szymanski.widgets.ListWidget
import me.szymanski.getValue
import me.szymanski.setValue

class RepositoriesList(ctx: Context, parent: ViewGroup? = null) : ViewWidget {

    private val errorView: TextView
    private val emptyView: TextView
    private val listWidget: ListWidget

    override val root = inflate(ctx, R.layout.screen_repos_list, parent).apply {
        errorView = reposErrorText
        emptyView = reposEmptyText
        listWidget = ListWidget(ctx, reposMainFrame)
        reposMainFrame.addView(listWidget.root)
    }

    var refreshing by listWidget::refreshing
    var listVisible by listWidget.root::isVisible
    var emptyTextVisible by emptyView::isVisible
    var errorTextVisible by errorView::isVisible
    var items by listWidget::items
    val refreshAction = listWidget.refreshAction
}
