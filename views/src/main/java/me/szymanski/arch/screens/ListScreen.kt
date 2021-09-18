package me.szymanski.arch.screens

import android.content.Context
import android.view.ViewGroup
import me.szymanski.arch.getValue
import me.szymanski.arch.inflate
import me.szymanski.arch.refreshing
import me.szymanski.arch.setValue
import me.szymanski.arch.widgets.ErrorBarWidget
import me.szymanski.arch.widgets.TextInputWidget
import me.szymanski.arch.widgets.databinding.ScreenReposListBinding
import me.szymanski.arch.widgets.list.ListWidget

class ListScreen(ctx: Context, parent: ViewGroup? = null) {

    private val errorWidget: ErrorBarWidget
    private val listWidget: ListWidget
    private val userNameInput: TextInputWidget

    val root = ctx.inflate(ScreenReposListBinding::inflate, parent).apply {
        userNameInput = listInput
        errorWidget = listErrorBar
        listWidget = listListWidget
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
