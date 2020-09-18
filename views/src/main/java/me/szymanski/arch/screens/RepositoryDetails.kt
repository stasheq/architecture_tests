package me.szymanski.arch.screens

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import com.example.widgets.R
import kotlinx.android.synthetic.main.screen_repo_details.view.*
import me.szymanski.arch.ViewWidget
import me.szymanski.arch.getValue
import me.szymanski.arch.setValue
import me.szymanski.arch.textValue
import me.szymanski.arch.widgets.ErrorWidget

class RepositoryDetails(ctx: Context, parent: ViewGroup? = null) : ViewWidget {
    private val errorWidget: ErrorWidget
    private val loadingView: View
    private val titleView: TextView

    override val root = inflate(ctx, R.layout.screen_repo_details, parent).apply {
        errorWidget = ErrorWidget(ctx, detailsMainFrame)
        loadingView = detailsLoading
        titleView = detailsTitle
        detailsMainFrame.addView(errorWidget.root)
    }

    var errorText by errorWidget::errorText
    var loading by loadingView::isVisible
    var title by titleView::textValue
    var detailsVisible by titleView::isVisible
}