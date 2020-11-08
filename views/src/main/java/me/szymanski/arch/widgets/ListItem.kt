package me.szymanski.arch.widgets

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item.view.*
import me.szymanski.arch.ViewWidget
import me.szymanski.arch.ViewWidget.Companion.inflate

data class ListItemData(val id: String, val text: String?, val description: String?)

class ListItem(parent: ViewGroup) : ViewWidget,
    RecyclerView.ViewHolder(inflate(parent.context, R.layout.list_item, parent)) {
    override val root: View = itemView
    val title: TextView = root.itemTitle
    val description: TextView = root.itemDescription
    val clickArea: View = root.itemClickArea

    fun bind(data: ListItemData, onClick: (String) -> Unit) {
        description.text = data.description
        title.text = data.text
        clickArea.setOnClickListener { onClick(data.id) }
    }
}
