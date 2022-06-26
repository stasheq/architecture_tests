package me.szymanski.arch.screens

import android.content.Context
import android.view.ViewGroup
import me.szymanski.arch.getValue
import me.szymanski.arch.inflate
import me.szymanski.arch.refreshing
import me.szymanski.arch.setValue
import me.szymanski.arch.widgets.databinding.ScreenReposListBinding

class ListScreen(ctx: Context, parent: ViewGroup? = null) : Screen {

    private val binding = ctx.inflate(ScreenReposListBinding::inflate, parent)
    override val root = binding.root

    var refreshing by binding.listListWidget::refreshing
    var errorText by binding.listErrorBar::errorText
    var items by binding.listListWidget::items
    var userName by binding.listInput::textValue
    val refreshAction = binding.listListWidget.refreshAction
    val userNameChanges = binding.listInput.textValueChanges
    var hasNextPage by binding.listListWidget::loadingNextPageIndicator
    val loadNextPageAction = binding.listListWidget.loadNextPageAction
    var lastItemMessage by binding.listListWidget::lastItemMessage
}
