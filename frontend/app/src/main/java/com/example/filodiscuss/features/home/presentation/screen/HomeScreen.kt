package com.example.filodiscuss.features.home.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.filodiscuss.features.auth.presentation.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(authViewModel: AuthViewModel = hiltViewModel(), onLogoutClick: () -> Unit) {
    val currentUser by authViewModel.currentUserState.collectAsState()
    LaunchedEffect(Unit) {
        authViewModel.getCurrentUser()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = currentUser?.username ?: "Welcome",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                actions = {
                    if (currentUser != null) {
                        Button(
                            onClick = onLogoutClick,
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                        ) {
                            Text(text = "Logout", color = Color.White)
                        }
                    }
                },
                colors = topAppBarColors(
                    containerColor = Color(0xFF6200EE),
                )
            )
        },
        content = { paddingValues ->
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                if (currentUser != null) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Hello, ${currentUser?.username}",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    )
}