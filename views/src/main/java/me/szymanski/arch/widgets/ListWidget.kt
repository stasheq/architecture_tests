package me.szymanski.arch.widgets

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

    inner class ListAdapter : RecyclerView.Adapter<ListItem>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ListItem(parent)
        override fun getItemCount() = items.size
        override fun onBindViewHolder(holder: ListItem, position: Int) {
            val item = items[position]
            holder.bind(item) { selectAction.accept(it) }
        }
    }
}
