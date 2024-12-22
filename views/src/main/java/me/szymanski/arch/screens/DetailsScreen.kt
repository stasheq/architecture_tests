package me.szymanski.arch.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.twotone.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import me.szymanski.arch.Style.barTextSize
import me.szymanski.arch.Style.dividerThickness
import me.szymanski.arch.Style.fontFamily
import me.szymanski.arch.Style.infoIconSize
import me.szymanski.arch.Style.infoIconTextSize
import me.szymanski.arch.Style.layoutPadding
import me.szymanski.arch.Style.textPadding
import me.szymanski.arch.Style.textSize
import me.szymanski.arch.Style.titleTextSize
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
    Toolbar(title, onBackIconDescription, onBackClick)
    Box(
        modifier = Modifier.background(colorResource(R.color.background))
    ) {
        Error(error, errorIconDescription)
        LoadingIndicator(isLoading)
        ItemsList(isListVisible, items)
    }
}

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Toolbar(title: State<String>, onBackIconDescription: String, onBackClick: () -> Unit) = TopAppBar(
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
                contentDescription = onBackIconDescription
            )
        }
    },
)

@Composable
fun ItemsList(
    isListVisible: State<Boolean>,
    items: State<List<ListItemType.ListItem>>,
) = if (isListVisible.value) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        items.value.forEach { Item(it) }
        Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.systemBars))
    }
} else Unit


@Composable
fun Item(item: ListItemType.ListItem) = Column(
    modifier = Modifier.padding(horizontal = layoutPadding)
) {
    Text(
        text = item.text ?: "",
        modifier = Modifier.padding(top = layoutPadding),
        fontSize = titleTextSize,
        fontWeight = FontWeight.Bold,
        fontFamily = fontFamily,
    )
    Text(
        text = item.description ?: "",
        modifier = Modifier.padding(top = textPadding),
        fontSize = textSize,
        fontFamily = fontFamily,
    )
    HorizontalDivider(
        modifier = Modifier.padding(top = layoutPadding),
        thickness = dividerThickness,
        color = colorResource(R.color.divider),
    )
}

@Composable
fun LoadingIndicator(isLoading: State<Boolean>) = if (isLoading.value)
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.width(64.dp)
        )
    } else Unit
