package me.szymanski.arch.widgets.list

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import me.szymanski.arch.ViewWidget
import me.szymanski.arch.widgets.R

class ListLoadingItem(parent: ViewGroup) : ViewWidget,
    RecyclerView.ViewHolder(inflate(parent.context, R.layout.list_loading_item, parent)) {
    override val root: View = itemView
}
