package me.szymanski.arch.designlib.listitem

sealed class ListItemType() {

    data class ListItem(
        val id: String,
        val text: String?,
        val description: String?,
        var onClick: (() -> Unit)? = null,
    ) : ListItemType()
}
