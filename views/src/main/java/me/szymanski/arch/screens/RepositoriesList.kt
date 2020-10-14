package me.szymanski.arch.screens

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.screen_repos_list.view.*
import me.szymanski.arch.widgets.ListWidget
import me.szymanski.arch.getValue
import me.szymanski.arch.setValue
import me.szymanski.arch.textValue
import me.szymanski.arch.widgets.ErrorWidget
import me.szymanski.arch.widgets.R
import me.szymanski.arch.widgets.TextInputWidget
import me.szymanski.glue.ViewWidget

class RepositoriesList(ctx: Context, parent: ViewGroup? = null) : ViewWidget {

    private val errorWidget: ErrorWidget
    private val emptyView: TextView
    private val listWidget: ListWidget
    private val userNameInput: TextInputWidget

    override val root = inflate(ctx, R.layout.screen_repos_list, parent).apply {
        errorWidget = ErrorWidget(ctx, reposMainFrame)
        emptyView = reposEmptyText
        userNameInput = TextInputWidget(ctx, reposLinearLayout)
        reposLinearLayout.addView(userNameInput.root)
        listWidget = ListWidget(ctx, reposLinearLayout)
        reposLinearLayout.addView(listWidget.root)
    }

    var refreshing by listWidget::refreshing
    var listVisible by listWidget.root::isVisible
    var emptyText by emptyView::textValue
    var errorText by errorWidget::errorText
    var items by listWidget::items
    var userName by userNameInput::textValue
    val refreshAction = listWidget.refreshAction
    val selectAction = listWidget.selectAction
    val userNameChanges = userNameInput.textValueChanges
}
