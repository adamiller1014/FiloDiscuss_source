package com.example.filodiscuss.features.home.presentation.screen.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.filodiscuss.features.home.domain.model.Post
import com.example.filodiscuss.features.home.presentation.PostViewModel
import kotlinx.coroutines.launch

@Composable
fun PostList(
    modifier: Modifier = Modifier,
    postViewModel: PostViewModel = hiltViewModel(),
    posts: List<Post>,
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
            PostItem(
                post = post,
                upVote = {
                    if (post.voteStatus == 1) return@PostItem
                    postViewModel.vote(postId = post.id.toInt(), value = 1)
                },
                downVote = {
                    if (post.voteStatus == -1) return@PostItem
                    postViewModel.vote(postId = post.id.toInt(), value = -1)
                }
            )
        }
        item {
            if (postViewModel.isLoadingMore) {
                LoadingIndicator()
            }
        }
    }

    LaunchedEffect(listState) {
        coroutineScope.launch {
            snapshotFlow { listState.layoutInfo.visibleItemsInfo }
                .collect { visibleItemsInfo ->
                    if (visibleItemsInfo.isNotEmpty() && visibleItemsInfo.last().index == listState.layoutInfo.totalItemsCount - 1) {
                        postViewModel.loadMorePosts()
                    }
                }
        }
    }
}

@Composable
fun LoadingIndicator() {
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
    upVote: () -> Unit,
    downVote: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.elevatedCardElevation()
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Column(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .width(40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(onClick = upVote) {
                    Icon(
                        imageVector = Icons.Filled.ArrowUpward,
                        contentDescription = "Upvote",
                        tint = if (post.voteStatus == 1) MaterialTheme.colorScheme.primary else Color.Gray
                    )
                }
                Text(
                    text = post.points.toInt().toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                IconButton(onClick = downVote) {
                    Icon(
                        imageVector = Icons.Filled.ArrowDownward,
                        contentDescription = "Downvote",
                        tint = if (post.voteStatus == -1) MaterialTheme.colorScheme.error else Color.Gray
                    )
                }
            }
            Column {
                Text(
                    text = "Posted by u/${post.creator?.username}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(4.dp))
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
}