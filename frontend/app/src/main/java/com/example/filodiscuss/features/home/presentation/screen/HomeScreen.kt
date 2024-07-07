package com.example.filodiscuss.features.home.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.filodiscuss.features.auth.presentation.AuthViewModel

@Composable
fun HomeScreen(authViewModel: AuthViewModel = hiltViewModel()) {
    val currentUser by authViewModel.currentUserState.collectAsState()
    LaunchedEffect(Unit) {
        authViewModel.getCurrentUser()
    }
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (currentUser != null) {
            Text(text = "Hello, ${currentUser?.username}")
        } else {
            Text(text = "User not logged in")
        }
    }
}