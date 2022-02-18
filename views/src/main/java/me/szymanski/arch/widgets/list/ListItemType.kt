package me.szymanski.arch.widgets.list

sealed class ListItemType(val type: Int) {

    data class ListItem(
        val id: String,
        val text: String?,
        val description: String?,
        val item: Any
    ) : ListItemType(type) {
        companion object {
            const val type = 0
        }
    }

    data class MessageItem(
        val text: String
    ) : ListItemType(type) {
        companion object {
            const val type = 1
        }
    }

    object LoadingItem : ListItemType(2)
}
