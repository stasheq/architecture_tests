package me.szymanski.arch.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import me.szymanski.arch.Style.barIconSize
import me.szymanski.arch.Style.barInputPadding
import me.szymanski.arch.Style.barTextSize
import me.szymanski.arch.Style.fontFamily
import me.szymanski.arch.designlib.Error
import me.szymanski.arch.designlib.listitem.ListItem
import me.szymanski.arch.designlib.Loading
import me.szymanski.arch.widgets.R
import me.szymanski.arch.designlib.listitem.ListItemType
import me.szymanski.arch.designlib.listitem.LoadingNextPageItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    items: State<List<ListItemType.ListItem>>,
    isListVisible: State<Boolean>,
    centerLoading: State<Boolean>,
    pullLoading: State<Boolean>,
    hasNextPage: State<Boolean>,
    error: State<String?>,
    errorIconDescription: String,
    defaultValue: String,
    onValueChange: (String) -> Unit,
    searchIconDescription: String,
    onPullToRefresh: () -> Unit,
    onLoadNextPage: () -> Unit
) = Column {
    ListToolbar(defaultValue, onValueChange, searchIconDescription)
    PullToRefreshBox(
        isRefreshing = pullLoading.value,
        onRefresh = onPullToRefresh,
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.background))
    ) {
        Error(error, errorIconDescription)
        Loading(centerLoading)
        ItemsList(isListVisible, items, hasNextPage, onLoadNextPage)
    }
}

@Composable
fun ItemsList(
    isListVisible: State<Boolean>,
    items: State<List<ListItemType.ListItem>>,
    hasNextPage: State<Boolean>,
    onLoadNextPage: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        if (isListVisible.value) {
            val itemsList = items.value
            items(itemsList.size) {
                ListItem(itemsList[it])
            }
            if (hasNextPage.value) {
                item {
                    LoadingNextPageItem(onLoadNextPage)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListToolbar(
    defaultValue: String,
    onValueChange: (String) -> Unit,
    searchIconDescription: String,
) {
    val inputValue = remember { mutableStateOf(defaultValue) }
    TopAppBar(
        title = {
            OutlinedTextField(
                value = inputValue.value,
                singleLine = true,
                onValueChange = {
                    inputValue.value = it
                    onValueChange(it)
                },
                textStyle = TextStyle(
                    fontSize = barTextSize,
                    fontFamily = fontFamily,
                    color = colorResource(R.color.barText),
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = barInputPadding),
                colors = OutlinedTextFieldDefaults.colors().copy(
                    focusedIndicatorColor = colorResource(R.color.barText),
                    unfocusedIndicatorColor = colorResource(R.color.barTextAlpha),
                )
            )
        },
        navigationIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                tint = colorResource(R.color.barText),
                contentDescription = searchIconDescription,
                modifier = Modifier.width(barIconSize)
            )
        },
        colors = topAppBarColors(
            containerColor = colorResource(R.color.colorPrimary),
        ),
    )
}
