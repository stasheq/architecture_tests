package me.szymanski.arch.widgets.list

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.jakewharton.rxrelay3.BehaviorRelay
import io.reactivex.rxjava3.core.Observable
import kotlinx.android.synthetic.main.list.view.*
import me.szymanski.arch.*
import me.szymanski.arch.ViewWidget.Companion.inflate
import me.szymanski.arch.widgets.R
import kotlin.collections.ArrayList

class ListWidget(ctx: Context, parent: ViewGroup? = null) : ViewWidget {
    var items: List<ListItemData> = ArrayList()
        set(value) {
            field = value
            adapter.notifyDataSetChanged()
        }
    private val adapter = ListAdapter()
    private val refreshLayout: SwipeRefreshLayout
    override val root: View = inflate(ctx, R.layout.list, parent).apply {
        reposRecycler.adapter = adapter
        reposRecycler.layoutManager = LinearLayoutManager(ctx, RecyclerView.VERTICAL, false)
        refreshLayout = reposSwipeRefresh
    }
    var refreshing: Boolean by refreshLayout::refreshing
    val refreshAction: Observable<Unit> = refreshLayout.refreshes()
    val selectAction: BehaviorRelay<String> = BehaviorRelay.create<String>()
    val loadNextPageAction: BehaviorRelay<Unit> = BehaviorRelay.create<Unit>()
    var loadingNextPageIndicator: Boolean = false
        set(value) {
            val changed = field != value
            val added = changed && value
            val removed = changed && !value
            field = value
            if (added) adapter.notifyItemInserted(items.size)
            if (removed) adapter.notifyItemRemoved(items.size)
        }
    var lastItemMessage: String? = null
        set(value) {
            val changed = field != value
            val added = changed && value != null
            val removed = changed && value == null
            field = value
            if (added) adapter.notifyItemInserted(items.size)
            if (removed) adapter.notifyItemRemoved(items.size)
            if (changed) adapter.notifyItemChanged(items.size)
        }

    inner class ListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        private val typeItem = 0
        private val typeLoading = 1
        private val typeMessage = 2

        init {
            setHasStableIds(true)
        }

        override fun getItemViewType(position: Int): Int = when {
            position < items.size -> typeItem
            loadingNextPageIndicator -> typeLoading
            lastItemMessage != null -> typeMessage
            else -> throw IllegalArgumentException("Can't find type for position $position")
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = when (viewType) {
            typeItem -> ListItem(parent)
            typeLoading -> ListLoadingItem(parent)
            typeMessage -> ListMessageItem(parent)
            else -> throw IllegalArgumentException("Can't create ViewHolder for type $viewType")
        }

        override fun getItemCount() = items.size +
                (if (loadingNextPageIndicator || lastItemMessage != null) 1 else 0)

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            when (holder) {
                is ListItem -> holder.bind(items[position]) { selectAction.accept(it) }
                is ListMessageItem -> lastItemMessage?.let { holder.bind(it) }
                is ListLoadingItem -> loadNextPageAction.accept(Unit)
            }
        }
    }
}
