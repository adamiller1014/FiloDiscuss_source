package com.example.filodiscuss.features.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filodiscuss.features.home.domain.repository.PostRepository
import com.example.filodiscuss.features.home.presentation.state.PostListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel  @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {

    private val _postListState = MutableStateFlow<PostListState>(PostListState.Idle)
    val postListState: StateFlow<PostListState> = _postListState

    fun getPosts() {
        _postListState.value = PostListState.Loading
        viewModelScope.launch {
            postRepository.getPost().collect { result ->
                result.onSuccess { posts ->
                    if (posts != null) {
                        _postListState.value = PostListState.Success(posts)
                    } else {
                        _postListState.value = PostListState.Error("No posts found.")
                    }
                }.onFailure { exception ->
                    _postListState.value = PostListState.Error(exception.message ?: "Unknown error")
                }
            }
        }
    }

}