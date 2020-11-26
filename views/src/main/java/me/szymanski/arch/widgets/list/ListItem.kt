package me.szymanski.arch.widgets.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.szymanski.arch.ViewHolder
import me.szymanski.arch.ViewWidget
import me.szymanski.arch.widgets.databinding.ListItemBinding

data class ListItemData(val id: String, val text: String?, val description: String?)

class ListItem(parent: ViewGroup) : ViewWidget, ViewHolder<ListItemBinding>(
    ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
) {
    override val root: View = binding.root

    fun bind(data: ListItemData, onClick: (String) -> Unit) {
        binding.itemDescription.text = data.description
        binding.itemTitle.text = data.text
        binding.itemClickArea.setOnClickListener { onClick(data.id) }
    }
}
