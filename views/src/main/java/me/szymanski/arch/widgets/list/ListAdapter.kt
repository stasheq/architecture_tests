package me.szymanski.arch.widgets.list

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import me.szymanski.arch.widgets.list.ListItemType.ListItem
import me.szymanski.arch.widgets.list.ListItemType.LoadingItem
import me.szymanski.arch.widgets.list.ListItemType.MessageItem
import me.szymanski.arch.widgets.list.viewholders.ListItemViewHolder
import me.szymanski.arch.widgets.list.viewholders.LoadingItemViewHolder
import me.szymanski.arch.widgets.list.viewholders.MessageItemViewHolder

class ListAdapter : androidx.recyclerview.widget.ListAdapter<ListItemType, RecyclerView.ViewHolder>(
    ListDiffUtilCallback()
) {
    var items: List<ListItem> = emptyList()
        set(value) {
            field = value
            updateList()
        }
    var loadingNextPageIndicator: Boolean = false
        set(value) {
            field = value
            updateList()
        }
    var lastItemMessage: String? = null
        set(value) {
            field = value
            updateList()
        }
    var selectItemAction: ((item: Any) -> Unit)? = null

    private val cells = mutableListOf<ListItemType>()

    private fun updateList() {
        val newCells = mutableListOf<ListItemType>().apply {
            addAll(items)
            if (loadingNextPageIndicator) add(LoadingItem)
            else lastItemMessage?.let { add(MessageItem(it)) }
        }
        cells.clear()
        cells.addAll(newCells)
        submitList(newCells)
    }

    override fun getItemViewType(position: Int): Int = cells[position].type

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = when (viewType) {
        ListItem.type -> ListItemViewHolder(parent)
        LoadingItem.type -> LoadingItemViewHolder(parent)
        MessageItem.type -> MessageItemViewHolder(parent)
        else -> throw IllegalArgumentException("Can't create ViewHolder for type $viewType")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ListItemViewHolder -> holder.bind(getItem(position) as ListItem, selectItemAction)
            is MessageItemViewHolder -> lastItemMessage?.let { holder.bind(it) }
        }
    }
}
