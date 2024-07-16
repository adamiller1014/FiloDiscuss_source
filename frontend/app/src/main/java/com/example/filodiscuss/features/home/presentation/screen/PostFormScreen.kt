package com.example.filodiscuss.features.home.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.filodiscuss.cors.navigation.Route
import com.example.filodiscuss.features.home.presentation.PostViewModel
import com.example.filodiscuss.features.home.presentation.state.CreatePostState

@Composable
fun PostFormScreen(
    postViewModel: PostViewModel = hiltViewModel(),
    onCancel: () -> Unit,
    navHostController: NavHostController
) {
    val createPostState by postViewModel.createPostState.collectAsState()

    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(createPostState) {
        when (createPostState) {
            is CreatePostState.Success -> {
                navHostController.navigate(Route.HomeScreen.name) {
                    popUpTo(navHostController.graph.startDestinationId) { inclusive = true }
                }
            }
            is CreatePostState.Error -> {
                snackbarHostState.showSnackbar((createPostState as CreatePostState.Error).message)
            }
            else -> {}
        }
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("Content") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(onClick = onCancel) {
                Text(text = "Cancel")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = { postViewModel.createPost(title, content) },
                enabled = title.isNotBlank() && content.isNotBlank()
            ) {
                Text(text = "Submit")
            }
        }

        if (createPostState is CreatePostState.Loading) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            }
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            SnackbarHost(hostState = snackbarHostState)
        }
    }
}
