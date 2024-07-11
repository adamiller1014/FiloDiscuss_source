package com.example.filodiscuss.features.home.presentation.screen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.filodiscuss.features.home.domain.model.Post
import com.example.filodiscuss.features.home.presentation.state.PostListState

@Composable
fun PostList(
    posts : List<Post>
) {

        LazyColumn(
            contentPadding = PaddingValues(
                top =  15.dp,
                bottom = 15.dp,
                start = 20.dp,
                end = 20.dp
            )
        ) {
            items(posts) { post ->
                ListItem(
                    headlineContent = {
                        Text(text = post.title)
                    },
                    modifier = Modifier.clickable {

                    }
                )
            }
        }
    }

