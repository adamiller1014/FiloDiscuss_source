package com.example.filodiscuss.features.home.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.filodiscuss.features.home.presentation.PostViewModel
import com.example.filodiscuss.features.home.presentation.state.PostDetailState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailScreen(
    postId: Int,
    postViewModel: PostViewModel = hiltViewModel(),
    onCancel: () -> Unit
) {
    val postDetailState by postViewModel.postDetailState.collectAsState()

    LaunchedEffect(Unit) {
        postViewModel.getPost(postId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Post Detail") },
                navigationIcon = {
                    IconButton(onClick = { onCancel() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* TODO: Handle upvote action */ },
                content = { Icon(Icons.Filled.ArrowUpward, contentDescription = "Upvote") }

            )
        },
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->
        when (val state = postDetailState) {
            is PostDetailState.Loading -> {
                Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is PostDetailState.Success -> {
                val post = state.post
                Column(modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)) {
                    Text(text = "Posted by u/${post.creator?.username}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = post.title, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSurface)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = post.content, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                }
            }
            is PostDetailState.Error -> {
                Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                    Text(text = "Error loading post", color = MaterialTheme.colorScheme.error)
                }
            }
            else -> {}
        }
    }
}
