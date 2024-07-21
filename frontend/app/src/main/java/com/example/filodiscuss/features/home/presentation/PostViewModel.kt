package com.example.filodiscuss.features.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filodiscuss.features.home.domain.repository.PostRepository
import com.example.filodiscuss.features.home.presentation.state.CreatePostState
import com.example.filodiscuss.features.home.presentation.state.PostDetailState
import com.example.filodiscuss.features.home.presentation.state.PostListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {

    private val _postListState = MutableStateFlow<PostListState>(PostListState.Idle)
    val postListState: StateFlow<PostListState> = _postListState

    private val _createPostState = MutableStateFlow<CreatePostState>(CreatePostState.Idle)
    val createPostState: StateFlow<CreatePostState> = _createPostState

    private val _postDetailState = MutableStateFlow<PostDetailState>(PostDetailState.Idle)
    val postDetailState: StateFlow<PostDetailState> = _postDetailState

    private var limit: Int = 10
    private var cursor: String? = null
    private var hasMore: Boolean = true
    var isLoadingMore: Boolean = false

    init {
        getPosts()
    }

    fun getPosts() {
        _postListState.value = PostListState.Loading
        viewModelScope.launch {
            postRepository.getPosts(cursor, limit).collect { result ->
                result.onSuccess { postResponse ->
                    cursor = postResponse.posts.lastOrNull()?.createdAt
                    hasMore = postResponse.hasMore
                    _postListState.value = PostListState.Success(postResponse.posts)
                }.onFailure { exception ->
                    _postListState.value = PostListState.Error(exception.message ?: "Unknown error")
                }
            }
        }
    }

    fun loadMorePosts() {
        if (isLoadingMore || !hasMore) return // Avoid multiple requests and check if there are more posts

        isLoadingMore = true
        viewModelScope.launch {
            postRepository.getPosts(cursor, limit).collect { result ->
                result.onSuccess { postResponse ->
                    val posts = postResponse.posts
                    cursor = posts.lastOrNull()?.createdAt
                    hasMore = postResponse.hasMore
                    val currentPosts = (_postListState.value as? PostListState.Success)?.posts ?: emptyList()
                    val updatedPosts = currentPosts + posts
                    _postListState.value = PostListState.Success(updatedPosts)
                }.onFailure { exception ->
                    _postListState.value = PostListState.Error(exception.message ?: "Unknown error")
                }
                isLoadingMore = false
            }
        }
    }

    fun createPost(title: String, content: String) {
        _createPostState.value = CreatePostState.Loading
        viewModelScope.launch {
            postRepository.createPost(title, content).collect { result ->
                result.onSuccess { post ->
                    if (post != null) _createPostState.value = CreatePostState.Success(post)
                }.onFailure { exception ->
                    _createPostState.value = CreatePostState.Error(exception.message ?: "Unknown error")
                }
            }
        }
    }

    fun vote(postId: Int, value: Int) {
        viewModelScope.launch {
            postRepository.vote(postId, value).collect { result ->
                result.onSuccess { isVoted ->
                    if (isVoted == true) {
                        _postListState.value = (_postListState.value as? PostListState.Success)?.let { postListState ->
                            val updatedPosts = postListState.posts.map { post ->
                                if (post.id.toInt() == postId) {
                                    val newPoints = when {
                                        post.voteStatus?.toInt() == value -> post.points.toInt()
                                        post.voteStatus == null || post.voteStatus == 0 -> post.points.toInt() + value
                                        else -> post.points.toInt() + value * 2
                                    }
                                    post.copy(
                                        points = newPoints,
                                        voteStatus = value
                                    )
                                } else post
                            }
                            PostListState.Success(updatedPosts)
                        } ?: _postListState.value
                    }
                }.onFailure {
                    // Handle error if needed
                }
            }
        }
    }

    fun getPost(postId: Int) {
        _postDetailState.value = PostDetailState.Loading
        viewModelScope.launch {
            postRepository.getPost(postId).collect { result ->
                result.onSuccess { post ->
                    _postDetailState.value = post?.let { PostDetailState.Success(it) }!!
                }.onFailure { exception ->
                    _postDetailState.value = PostDetailState.Error(exception.message ?: "Unknown error")
                }
            }
        }
    }

    fun deletePost(postId: Int) {
        viewModelScope.launch {
            postRepository.deletePost(postId).collect { result ->
                result.onSuccess {
                    _postListState.value = (_postListState.value as? PostListState.Success)?.let { postListState ->
                        val updatedPosts = postListState.posts.filterNot { it.id.toInt() == postId }
                        PostListState.Success(updatedPosts)
                    } ?: _postListState.value
                }.onFailure { exception ->
                    // Handle error
                    _postListState.value = PostListState.Error(exception.message ?: "Unknown error")
                }
            }
        }
    }

}