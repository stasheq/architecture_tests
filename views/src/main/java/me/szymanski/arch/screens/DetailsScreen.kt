package me.szymanski.arch.screens

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import me.szymanski.arch.getValue
import me.szymanski.arch.inflate
import me.szymanski.arch.setValue
import me.szymanski.arch.widgets.databinding.ScreenRepoDetailsBinding

class DetailsScreen(binding: ScreenRepoDetailsBinding) : Screen {
    constructor(ctx: Context, parent: ViewGroup? = null) : this(ctx.inflate(ScreenRepoDetailsBinding::inflate, parent))
    constructor(view: View) : this(ScreenRepoDetailsBinding.bind(view))

    init {
        binding.apply {
            detailsToolbar.showBackIcon = true
            detailsList.refreshingEnabled = false
        }
    }

    override val root = binding.root

    var errorText by binding.detailsErrorBar::errorText
    var loading by binding.detailsLoading::isVisible
    var title by binding.detailsToolbar::title
    var backClick by binding.detailsToolbar::backClick
    var showBackIcon by binding.detailsToolbar::showBackIcon
    var items by binding.detailsList::items
    var listVisible by binding.detailsList::isVisible
}
