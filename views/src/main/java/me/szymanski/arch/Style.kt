package me.szymanski.arch

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.szymanski.arch.widgets.R

object Style {
    val layoutPadding = 15.dp
    val textPadding = 6.dp
    val titleTextSize = 18.sp
    val textSize = 16.sp
    val barTextSize = 18.sp
    val infoIconTextSize = 24.sp
    val infoIconSize = 64.dp
    val dividerThickness = 1.dp

    val fontFamily = FontFamily(
        Font(R.font.lato_regular, FontWeight.Normal),
    )
}
