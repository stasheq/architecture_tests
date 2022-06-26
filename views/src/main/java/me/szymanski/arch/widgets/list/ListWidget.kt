package me.szymanski.arch.widgets.list

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import me.szymanski.arch.debounce
import me.szymanski.arch.getValue
import me.szymanski.arch.inflate
import me.szymanski.arch.mutableEventFlow
import me.szymanski.arch.refreshes
import me.szymanski.arch.setValue
import me.szymanski.arch.widgets.databinding.ListBinding
import me.szymanski.arch.widgets.list.ListItemType.ListItem
import me.szymanski.arch.widgets.list.ListItemType.LoadingItem

class ListWidget @JvmOverloads constructor(ctx: Context, attrs: AttributeSet? = null) : SwipeRefreshLayout(ctx, attrs) {

    private val adapter = ListAdapter()

    init {
        ctx.inflate(ListBinding::inflate, this).apply {
            reposRecycler.adapter = adapter
            val layoutManager = LinearLayoutManager(ctx, RecyclerView.VERTICAL, false)
            reposRecycler.layoutManager = layoutManager
            reposRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                val action = Runnable { loadNextPageAction.tryEmit(Unit) }
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val lastPos = layoutManager.findLastVisibleItemPosition()
                    if (adapter.getItemViewType(lastPos) == LoadingItem.type) debounce(500, action)
                }
            })
        }
    }

    var refreshingEnabled: Boolean = true
        set(value) {
            field = value
            isEnabled = value
        }
    val refreshAction = this.refreshes()
    val loadNextPageAction = mutableEventFlow<Unit>()
    var loadingNextPageIndicator: Boolean by adapter::loadingNextPageIndicator
    var lastItemMessage: String? by adapter::lastItemMessage
    var items: List<ListItem> by adapter::items
}
