package me.szymanski.arch.designlib.listitem

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import me.szymanski.arch.Style.dividerThickness
import me.szymanski.arch.Style.fontFamily
import me.szymanski.arch.Style.layoutPadding
import me.szymanski.arch.Style.textPadding
import me.szymanski.arch.Style.textSize
import me.szymanski.arch.Style.titleTextSize
import me.szymanski.arch.widgets.R

@Composable
fun ListItem(item: ListItemType.ListItem) = Column(
    modifier = Modifier.let {
        if (item.onClick != null)
            it.clickable(
                indication = ripple(),
                interactionSource = remember { MutableInteractionSource() },
                onClick = { item.onClick?.invoke() })
        else it
    }
) {
    Text(
        text = item.text ?: "",
        modifier = Modifier
            .padding(top = layoutPadding)
            .padding(horizontal = layoutPadding),
        fontSize = titleTextSize,
        fontWeight = FontWeight.Bold,
        fontFamily = fontFamily,
    )
    Text(
        text = item.description ?: "",
        modifier = Modifier
            .padding(top = textPadding)
            .padding(horizontal = layoutPadding),
        fontSize = textSize,
        fontFamily = fontFamily,
    )
    HorizontalDivider(
        modifier = Modifier
            .padding(top = layoutPadding)
            .padding(horizontal = layoutPadding),
        thickness = dividerThickness,
        color = colorResource(R.color.divider),
    )
}
