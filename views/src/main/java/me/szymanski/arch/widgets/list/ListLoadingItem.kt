package me.szymanski.arch.widgets.list

import android.view.View
import android.view.ViewGroup
import me.szymanski.arch.ViewHolder
import me.szymanski.arch.ViewWidget
import me.szymanski.arch.inflate
import me.szymanski.arch.widgets.databinding.ListLoadingItemBinding

class ListLoadingItem(parent: ViewGroup) : ViewWidget, ViewHolder<ListLoadingItemBinding>(
    parent.inflate(ListLoadingItemBinding::inflate)
) {
    override val root: View = binding.root
}
