package me.szymanski.arch.widgets.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.szymanski.arch.ViewHolder
import me.szymanski.arch.ViewWidget
import me.szymanski.arch.widgets.databinding.ListLoadingItemBinding

class ListLoadingItem(parent: ViewGroup) : ViewWidget, ViewHolder<ListLoadingItemBinding>(
    ListLoadingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
) {
    override val root: View = binding.root
}
