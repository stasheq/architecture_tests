package me.szymanski.arch.widgets.list

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.jakewharton.rxrelay3.PublishRelay
import io.reactivex.rxjava3.core.Observable
import me.szymanski.arch.*
import me.szymanski.arch.widgets.databinding.ListBinding
import me.szymanski.arch.getValue
import me.szymanski.arch.setValue

class ListWidget(ctx: Context, parent: ViewGroup? = null) : ViewWidget {

    private val adapter = ListAdapter()
    private val refreshLayout: SwipeRefreshLayout

    override val root: View = ctx.inflate(ListBinding::inflate, parent).apply {
        reposRecycler.adapter = adapter
        val layoutManager = LinearLayoutManager(ctx, RecyclerView.VERTICAL, false)
        reposRecycler.layoutManager = layoutManager
        reposRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val lastPos = layoutManager.findLastVisibleItemPosition()
                if (adapter.getItemViewType(lastPos) == ListAdapter.typeLoading) loadNextPageAction.accept(Unit)
            }
        })
        refreshLayout = reposSwipeRefresh
        selectingEnabled = true
    }.root

    var refreshingEnabled: Boolean = true
        set(value) {
            field = value
            refreshLayout.isEnabled = value
        }
    var selectingEnabled: Boolean = true
        set(value) {
            field = value
            adapter.selectItemAction = if (value) { id -> selectAction.accept(id) } else null
        }
    var refreshing: Boolean by refreshLayout::refreshing
    val refreshAction: Observable<Unit> = refreshLayout.refreshes()
    val selectAction: PublishRelay<String> = PublishRelay.create()
    val loadNextPageAction: PublishRelay<Unit> = PublishRelay.create()
    var loadingNextPageIndicator: Boolean by adapter::loadingNextPageIndicator
    var lastItemMessage: String? by adapter::lastItemMessage
    var items: List<ListItemData> by adapter::items
}
