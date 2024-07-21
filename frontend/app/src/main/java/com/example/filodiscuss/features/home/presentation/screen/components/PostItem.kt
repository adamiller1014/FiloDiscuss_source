package com.example.filodiscuss.features.home.presentation.screen.components

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.example.filodiscuss.features.home.domain.model.Post

@Composable
fun PostItem(
    post: Post,
    upVote: () -> Unit,
    downVote: () -> Unit,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    onEdit: (String, String) -> Unit
) {
    var showMenu by rememberSaveable { mutableStateOf(false) }
    var showEditDialog by rememberSaveable { mutableStateOf(false) }
    var showDeleteDialog by rememberSaveable { mutableStateOf(false) }
    var pressOffset by remember { mutableStateOf(DpOffset.Zero) }
    var itemHeight by remember { mutableStateOf(0.dp) }
    val interactionSource = remember { MutableInteractionSource() }
    val density = LocalDensity.current

    if (showEditDialog) {
        EditPostDialog(
            post = post,
            onDismiss = { showEditDialog = false },
            onEdit = { newTitle, newContent ->
                onEdit(newTitle, newContent)
                showEditDialog = false
            }
        )
    }

    if (showDeleteDialog) {
        DeletePostDialog(
            onDismiss = { showDeleteDialog = false },
            onDelete = {
                onDelete()
                showDeleteDialog = false
            }
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick)
            .onSizeChanged {
                itemHeight = with(density) { it.height.toDp() }
            },
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
                    text = post.points!!.toInt().toString(),
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
            Column(
                modifier = Modifier.weight(1f)
            ) {
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
            Box(
                modifier = Modifier
                    .indication(interactionSource, LocalIndication.current)
                    .pointerInput(true) {
                        detectTapGestures(
                            onLongPress = {
                                showMenu = true
                                pressOffset = DpOffset(it.x.toDp(), it.y.toDp())
                            },
                            onPress = {
                                val press = PressInteraction.Press(it)
                                interactionSource.emit(press)
                                tryAwaitRelease()
                                interactionSource.emit(PressInteraction.Release(press))
                            }
                        )
                    }
                    .align(Alignment.CenterVertically)
            ) {
                IconButton(onClick = { showMenu = !showMenu }) {
                    Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "More options")
                }

                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false },
                    offset = pressOffset.copy(y = pressOffset.y - itemHeight)
                ) {
                    DropdownMenuItem(
                        text = { Text(text = "Edit") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Filled.Edit, contentDescription = "Edit")
                        },
                        onClick = {
                            showMenu = false
                            showEditDialog = true
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(text = "Delete") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete")
                        },
                        onClick = {
                            showMenu = false
                            showDeleteDialog = true
                        }
                    )
                }
            }
        }
    }
}
