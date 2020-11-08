package me.szymanski.arch.widgets.list

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_message_item.view.*
import me.szymanski.arch.ViewWidget
import me.szymanski.arch.ViewWidget.Companion.inflate
import me.szymanski.arch.widgets.R

class ListMessageItem(parent: ViewGroup) : ViewWidget,
    RecyclerView.ViewHolder(inflate(parent.context, R.layout.list_message_item, parent)) {
    override val root: View = itemView
    val message: TextView = root.message

    fun bind(text: String) {
        message.text = text
    }
}
