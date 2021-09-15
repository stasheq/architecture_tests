package me.szymanski.arch.widgets.list

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import me.szymanski.arch.*
import me.szymanski.arch.widgets.databinding.ListBinding
import me.szymanski.arch.getValue
import me.szymanski.arch.setValue

class ListWidget @JvmOverloads constructor(ctx: Context, attrs: AttributeSet? = null) : SwipeRefreshLayout(ctx, attrs) {

    private val adapter = ListAdapter()

    init {
        ctx.inflate(ListBinding::inflate, this).apply {
            reposRecycler.adapter = adapter
            val layoutManager = LinearLayoutManager(ctx, RecyclerView.VERTICAL, false)
            reposRecycler.layoutManager = layoutManager
            reposRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val lastPos = layoutManager.findLastVisibleItemPosition()
                    if (adapter.getItemViewType(lastPos) == ListAdapter.typeLoading) loadNextPageAction.tryEmit(Unit)
                }
            })
            selectingEnabled = true
        }
    }

    var refreshingEnabled: Boolean = true
        set(value) {
            field = value
            isEnabled = value
        }
    var selectingEnabled: Boolean = true
        set(value) {
            field = value
            adapter.selectItemAction = if (value) { id -> selectAction.tryEmit(id) } else null
        }
    val refreshAction = this.refreshes()
    val selectAction = MutableSharedFlow<String>(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val loadNextPageAction = MutableSharedFlow<Unit>(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    var loadingNextPageIndicator: Boolean by adapter::loadingNextPageIndicator
    var lastItemMessage: String? by adapter::lastItemMessage
    var items: List<ListItemData> by adapter::items
}
