package me.szymanski.arch.widgets.list

import android.view.ViewGroup
import me.szymanski.arch.ViewHolder
import me.szymanski.arch.inflate
import me.szymanski.arch.widgets.databinding.ListMessageItemBinding

class ListMessageItem(parent: ViewGroup) : ViewHolder<ListMessageItemBinding>(
    parent.inflate(ListMessageItemBinding::inflate)
) {
    fun bind(text: String) {
        binding.message.text = text
    }
}
