package me.szymanski.widgets

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.widgets.R
import com.jakewharton.rxbinding4.swiperefreshlayout.refreshes
import kotlinx.android.synthetic.main.list.view.*
import kotlinx.android.synthetic.main.list_item.view.*
import kotlin.collections.ArrayList

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

    inner class ListAdapter : RecyclerView.Adapter<ListAdapter.ViewHolder>() {
        var onClick: ((id: ListItem) -> Unit)? = null

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = items[position]
            holder.description.text = item.description
            holder.title.text = item.text
            holder.clickArea.setOnClickListener {
                onClick?.invoke(item)
            }
        }

        override fun getItemCount() = items.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val title: TextView = view.itemTitle
            val description: TextView = view.itemDescription
            val clickArea: View = view.itemClickArea
        }
    }
}

data class ListItem(val text: String?, val description: String?)