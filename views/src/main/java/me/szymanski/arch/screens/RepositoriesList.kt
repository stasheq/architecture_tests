package me.szymanski.arch.screens

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.screen_repos_list.view.*
import me.szymanski.arch.ViewWidget
import me.szymanski.arch.ViewWidget.Companion.inflate
import me.szymanski.arch.getValue
import me.szymanski.arch.setValue
import me.szymanski.arch.textValue
import me.szymanski.arch.widgets.ErrorBar
import me.szymanski.arch.widgets.R
import me.szymanski.arch.widgets.TextInputWidget
import me.szymanski.arch.widgets.list.ListWidget

class RepositoriesList(ctx: Context, parent: ViewGroup? = null) : ViewWidget {

    private val errorWidget: ErrorBar
    private val emptyView: TextView
    private val listWidget: ListWidget
    private val userNameInput: TextInputWidget

    override val root = inflate(ctx, R.layout.screen_repos_list, parent).apply {
        errorWidget = ErrorBar(ctx, reposMainFrame)
        reposLinearLayout.addView(errorWidget.root)
        emptyView = reposEmptyText
        userNameInput = TextInputWidget(ctx, reposLinearLayout)
        reposLinearLayout.addView(userNameInput.root)
        listWidget = ListWidget(ctx, reposLinearLayout)
        reposLinearLayout.addView(listWidget.root)
    }

    var refreshing by listWidget::refreshing
    var emptyText by emptyView::textValue
    var errorText by errorWidget::errorText
    var items by listWidget::items
    var userName by userNameInput::textValue
    val refreshAction = listWidget.refreshAction
    val selectAction = listWidget.selectAction
    val userNameChanges = userNameInput.textValueChanges
    var hasNextPage by listWidget::loadingNextPageIndicator
    val loadNextPageAction = listWidget.loadNextPageAction
    var lastItemMessage by listWidget::lastItemMessage
}
