package me.szymanski.arch.widgets.list.viewholders

import android.view.ViewGroup
import me.szymanski.arch.ViewHolder
import me.szymanski.arch.inflate
import me.szymanski.arch.widgets.databinding.ListItemBinding
import me.szymanski.arch.widgets.list.ListItemType

class ListItemViewHolder(parent: ViewGroup) : ViewHolder<ListItemBinding>(
    parent.inflate(ListItemBinding::inflate)
) {
    fun bind(data: ListItemType.ListItem) {
        binding.itemDescription.text = data.description
        binding.itemTitle.text = data.text
        data.onClick.let {
            if (it == null) {
                binding.itemClickArea.isClickable = false
            } else {
                binding.itemClickArea.isClickable = true
                binding.itemClickArea.setOnClickListener { it() }
            }
        }
    }
}