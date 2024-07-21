package com.example.filodiscuss.features.home.presentation.screen.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.filodiscuss.cors.navigation.Route
import com.example.filodiscuss.features.home.domain.model.Post
import com.example.filodiscuss.features.home.presentation.PostViewModel
import kotlinx.coroutines.launch

@Composable
fun PostList(
    modifier: Modifier = Modifier,
    postViewModel: PostViewModel = hiltViewModel(),
    posts: List<Post>,
    navHostController: NavHostController
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
                },
                onClick = {
                    navHostController.navigate(Route.PostDetailScreen.name+"/${post.id.toInt()}")
                },
                onDelete = {
                    postViewModel.deletePost(postId = post.id.toInt())
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