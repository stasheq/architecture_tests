package me.szymanski.arch.widgets.list

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

class ListDiffUtilCallback<T : Any> : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean = oldItem == newItem

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean = oldItem == newItem
}
