package me.szymanski.arch.screens

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import me.szymanski.arch.ViewWidget
import me.szymanski.arch.getValue
import me.szymanski.arch.setValue
import me.szymanski.arch.widgets.ErrorBar
import me.szymanski.arch.widgets.TextInputWidget
import me.szymanski.arch.widgets.databinding.ScreenReposListBinding
import me.szymanski.arch.widgets.list.ListWidget

class ListScreen(ctx: Context, parent: ViewGroup? = null) : ViewWidget {

    private val errorWidget: ErrorBar
    private val listWidget: ListWidget
    private val userNameInput: TextInputWidget

    override val root = ScreenReposListBinding.inflate(
        LayoutInflater.from(ctx), parent, false
    ).apply {
        userNameInput = TextInputWidget(ctx, reposLinearLayout)
        reposLinearLayout.addView(userNameInput.root)
        errorWidget = ErrorBar(ctx, reposLinearLayout)
        reposLinearLayout.addView(errorWidget.root)
        listWidget = ListWidget(ctx, reposLinearLayout)
        reposLinearLayout.addView(listWidget.root)
    }.root

    var refreshing by listWidget::refreshing
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
