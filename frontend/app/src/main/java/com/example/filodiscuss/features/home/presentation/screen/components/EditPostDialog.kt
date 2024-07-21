package com.example.filodiscuss.features.home.presentation.screen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.filodiscuss.features.home.domain.model.Post

@Composable
fun EditPostDialog(
    post: Post,
    onDismiss: () -> Unit,
    onEdit: (String, String) -> Unit
) {
    var newTitle by remember { mutableStateOf(post.title) }
    var newContent by remember { mutableStateOf(post.content) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Post") },
        text = {
            Column {
                TextField(
                    value = newTitle,
                    onValueChange = { newTitle = it },
                    label = { Text("Title") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = newContent,
                    onValueChange = { newContent = it },
                    label = { Text("Content") },
                    maxLines = 5
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onEdit(newTitle, newContent)
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
