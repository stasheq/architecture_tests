package me.szymanski.arch.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import me.szymanski.arch.Style.barIconSize
import me.szymanski.arch.Style.barTextSize
import me.szymanski.arch.Style.fontFamily
import me.szymanski.arch.designlib.Error
import me.szymanski.arch.designlib.ListItem
import me.szymanski.arch.designlib.Loading
import me.szymanski.arch.widgets.R
import me.szymanski.arch.widgets.list.ListItemType
import kotlin.collections.forEach

@Composable
fun DetailsScreen(
    title: State<String>,
    items: State<List<ListItemType.ListItem>>,
    isListVisible: State<Boolean>,
    isLoading: State<Boolean>,
    error: State<String?>,
    errorIconDescription: String,
    onBackIconDescription: String,
    onBackClick: () -> Unit,
) = Column {
    DetailsToolbar(title, onBackIconDescription, onBackClick)
    Box(
        modifier = Modifier.background(colorResource(R.color.background))
    ) {
        Error(error, errorIconDescription)
        Loading(isLoading)
        DetailsItemsList(isListVisible, items)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsToolbar(
    title: State<String>,
    onBackIconDescription: String,
    onBackClick: () -> Unit,
) = TopAppBar(
    title = {
        Text(
            text = title.value,
            fontSize = barTextSize,
            fontFamily = fontFamily,
            color = colorResource(R.color.barText)
        )
    },
    colors = topAppBarColors(
        containerColor = colorResource(R.color.colorPrimary),
    ),
    navigationIcon = {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                tint = colorResource(R.color.barText),
                contentDescription = onBackIconDescription,
                modifier = Modifier.width(barIconSize)
            )
        }
    },
)

@Composable
fun DetailsItemsList(
    isListVisible: State<Boolean>,
    items: State<List<ListItemType.ListItem>>,
) = if (isListVisible.value) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        items.value.forEach { ListItem(it) }
        Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.systemBars))
    }
} else Unit
