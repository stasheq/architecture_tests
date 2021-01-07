package me.szymanski.arch.widgets.list

import android.view.View
import android.view.ViewGroup
import me.szymanski.arch.ViewHolder
import me.szymanski.arch.ViewWidget
import me.szymanski.arch.inflate
import me.szymanski.arch.widgets.databinding.ListMessageItemBinding

class ListMessageItem(parent: ViewGroup) : ViewWidget, ViewHolder<ListMessageItemBinding>(
    parent.inflate(ListMessageItemBinding::inflate)
) {
    override val root: View = binding.root

    fun bind(text: String) {
        binding.message.text = text
    }
}
