package me.szymanski.arch.widgets.list.viewholders

import android.view.ViewGroup
import me.szymanski.arch.ViewHolder
import me.szymanski.arch.inflate
import me.szymanski.arch.widgets.databinding.ListLoadingItemBinding

class LoadingItemViewHolder(parent: ViewGroup) : ViewHolder<ListLoadingItemBinding>(
    parent.inflate(ListLoadingItemBinding::inflate)
)
