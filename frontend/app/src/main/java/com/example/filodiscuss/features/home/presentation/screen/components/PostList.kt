package com.example.filodiscuss.features.home.presentation.screen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.filodiscuss.features.home.domain.model.Post
import kotlinx.coroutines.launch

@Composable
fun PostList(
    posts: List<Post>,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    onLoadMore: () -> Unit,
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LazyColumn(
        contentPadding = PaddingValues(
            top = 15.dp,
            bottom = 15.dp,
            start = 20.dp,
            end = 20.dp
        ),
        modifier = modifier,
        state = listState
    ) {
        items(posts) { post ->
            PostItem(post = post, onClick = { })
        }
        item {
            if (isLoading) {
                LoadingIndicator()
            }
        }
    }

    LaunchedEffect(listState) {
        coroutineScope.launch {
            snapshotFlow { listState.layoutInfo.visibleItemsInfo }
                .collect { visibleItemsInfo ->
                    if (visibleItemsInfo.isNotEmpty() && visibleItemsInfo.last().index == listState.layoutInfo.totalItemsCount - 1) {
                        onLoadMore()
                    }
                }
        }
    }
}

@Composable
fun LoadingIndicator() {
    // Add your loading indicator UI here
    Text(
        text = "Loading...",
        style = MaterialTheme.typography.bodyMedium,
        color = Color.Gray,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        textAlign = TextAlign.Center
    )
}



@Composable
fun PostItem(
    post: Post,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.elevatedCardElevation()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = post.title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = post.content,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
    }
}
