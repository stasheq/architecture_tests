package me.szymanski.arch.designlib

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import me.szymanski.arch.Style.fontFamily
import me.szymanski.arch.Style.infoIconSize
import me.szymanski.arch.Style.infoIconTextSize
import me.szymanski.arch.Style.layoutPadding
import me.szymanski.arch.widgets.R

@Composable
fun Error(error: State<String?>, errorIconDescription: String) = error.value?.let {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(layoutPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            modifier = Modifier.size(infoIconSize),
            imageVector = Icons.TwoTone.Warning,
            tint = colorResource(R.color.textSecondary),
            contentDescription = errorIconDescription
        )
        Text(
            modifier = Modifier.padding(top = layoutPadding),
            text = it,
            fontSize = infoIconTextSize,
            fontFamily = fontFamily,
            color = colorResource(R.color.textSecondary)
        )
    }
} ?: Unit
