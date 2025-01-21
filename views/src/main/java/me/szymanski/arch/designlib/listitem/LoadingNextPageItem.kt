package me.szymanski.arch.designlib.listitem

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.szymanski.arch.Style.layoutPadding

@Composable
fun LoadingNextPageItem(onLoadNextPage: () -> Unit) = Column(
    modifier = Modifier
) {
    onLoadNextPage()
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .width(32.dp)
                .padding(vertical = layoutPadding)
        )
    }
}
