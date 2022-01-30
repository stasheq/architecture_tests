package me.szymanski.arch.widgets.list

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ListAdapter : androidx.recyclerview.widget.ListAdapter<ListItemData, RecyclerView.ViewHolder>(
    ListDiffUtilCallback()
) {
    var items: List<ListItemData> = ArrayList()
        set(value) {
            field = value
            submitList(value)
        }
    var loadingNextPageIndicator: Boolean = false
        set(value) {
            val changed = field != value
            val added = changed && value
            val removed = changed && !value
            field = value
            if (added) notifyItemInserted(items.size)
            if (removed) notifyItemRemoved(items.size)
        }
    var lastItemMessage: String? = null
        set(value) {
            val changed = field != value
            val added = changed && value != null
            val removed = changed && value == null
            field = value
            if (added) notifyItemInserted(items.size)
            if (removed) notifyItemRemoved(items.size)
            if (changed) notifyItemChanged(items.size)
        }
    var selectItemAction: ((item: Any) -> Unit)? = null

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

    override fun getItemId(position: Int): Long = when (getItemViewType(position)) {
        typeItem -> items[position].id.hashCode().toLong()
        typeLoading -> Int.MAX_VALUE.toLong() + typeLoading.toLong()
        typeMessage -> Int.MAX_VALUE.toLong() + typeMessage.toLong()
        else -> throw IllegalArgumentException("Can't find type for position $position for id calculation")
    }

    override fun getItemCount() = items.size +
            (if (loadingNextPageIndicator || lastItemMessage != null) 1 else 0)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ListItem -> holder.bind(items[position], selectItemAction)
            is ListMessageItem -> lastItemMessage?.let { holder.bind(it) }
        }
    }

    companion object {
        const val typeItem = 0
        const val typeLoading = 1
        const val typeMessage = 2
    }
}
