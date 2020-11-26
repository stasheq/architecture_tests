package me.szymanski.arch.widgets.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.szymanski.arch.ViewHolder
import me.szymanski.arch.ViewWidget
import me.szymanski.arch.widgets.databinding.ListMessageItemBinding

class ListMessageItem(parent: ViewGroup) : ViewWidget, ViewHolder<ListMessageItemBinding>(
    ListMessageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
) {
    override val root: View = binding.root

    fun bind(text: String) {
        binding.message.text = text
    }
}
