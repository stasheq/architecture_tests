package me.szymanski.arch.widgets

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.jakewharton.rxbinding4.swiperefreshlayout.refreshes
import com.jakewharton.rxrelay3.BehaviorRelay
import kotlinx.android.synthetic.main.list.view.*
import kotlinx.android.synthetic.main.list_item.view.*
import me.szymanski.arch.ViewWidget
import me.szymanski.arch.refreshing
import kotlin.collections.ArrayList
import me.szymanski.arch.getValue
import me.szymanski.arch.setValue

class ListWidget(ctx: Context, parent: ViewGroup? = null) : ViewWidget {
    var items: List<ListItem> = ArrayList()
        set(value) {
            field = value
            adapter.notifyDataSetChanged()
        }
    private val adapter = ListAdapter()
    private val refreshLayout: SwipeRefreshLayout
    override val root: View = LayoutInflater.from(ctx).inflate(R.layout.list, parent, false).apply {
        reposRecycler.adapter = adapter
        reposRecycler.layoutManager = LinearLayoutManager(ctx, RecyclerView.VERTICAL, false)
        refreshLayout = reposSwipeRefresh
    }
    var refreshing: Boolean by refreshLayout::refreshing
    val refreshAction = refreshLayout.refreshes()
    val selectAction: BehaviorRelay<String> = BehaviorRelay.create<String>()

    inner class ListAdapter : RecyclerView.Adapter<ListAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = items[position]
            holder.description.text = item.description
            holder.title.text = item.text
            holder.clickArea.setOnClickListener { selectAction.accept(item.id) }
        }

        override fun getItemCount() = items.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val title: TextView = view.itemTitle
            val description: TextView = view.itemDescription
            val clickArea: View = view.itemClickArea
        }
    }
}

data class ListItem(val id: String, val text: String?, val description: String?)
