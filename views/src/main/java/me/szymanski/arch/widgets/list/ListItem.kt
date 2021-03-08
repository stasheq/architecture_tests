package me.szymanski.arch.widgets.list

import android.view.ViewGroup
import me.szymanski.arch.ViewHolder
import me.szymanski.arch.inflate
import me.szymanski.arch.widgets.databinding.ListItemBinding

data class ListItemData(val id: String, val text: String?, val description: String?)

class ListItem(parent: ViewGroup) : ViewHolder<ListItemBinding>(
    parent.inflate(ListItemBinding::inflate)
) {
    fun bind(data: ListItemData, onClick: ((String) -> Unit)?) {
        binding.itemDescription.text = data.description
        binding.itemTitle.text = data.text
        if (onClick == null) {
            binding.itemClickArea.isClickable = false
        } else {
            binding.itemClickArea.isClickable = true
            binding.itemClickArea.setOnClickListener { onClick(data.id) }
        }
    }
}
