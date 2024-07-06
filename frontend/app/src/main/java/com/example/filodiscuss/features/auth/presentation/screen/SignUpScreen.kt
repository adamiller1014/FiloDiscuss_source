package com.example.filodiscuss.features.auth.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.filodiscuss.cors.navigation.Route
import com.example.filodiscuss.features.auth.presentation.AuthViewModel
import com.example.filodiscuss.features.auth.presentation.screen.components.HeaderText
import com.example.filodiscuss.features.auth.presentation.screen.components.LoginTextField
import com.example.filodiscuss.features.auth.presentation.state.RegisterState
import com.example.filodiscuss.ui.theme.FiloDiscussTheme


@Composable
fun SignUpScreen(
    onLoginClick: () -> Unit,
    navHostController: NavHostController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val registerState by authViewModel.registerState.collectAsStateWithLifecycle()
    val (username, onUsernameChange) = rememberSaveable { mutableStateOf("") }
    val (password, onPasswordChange) = rememberSaveable { mutableStateOf("") }
    val isFieldsNotEmpty = username.isNotEmpty() && password.isNotEmpty()
    val snackbarHostState = remember { SnackbarHostState() }
    val signInText = "Sign In"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeaderText(
            text = "Sign Up",
            modifier = Modifier
                .padding(vertical = 16.dp)
                .align(alignment = Alignment.Start)
        )
        LoginTextField(
            value = username,
            onValueChange = onUsernameChange,
            labelText = "Username",
            leadingIcon = Icons.Default.Person,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        LoginTextField(
            value = password,
            onValueChange = onPasswordChange,
            labelText = "Password",
            leadingIcon = Icons.Default.Lock,
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Password,
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {
                authViewModel.register(username, password)
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = isFieldsNotEmpty,
        ) {
            Text("Sign Up")
        }

        Spacer(Modifier.height(16.dp))
        val signInAnnotation = buildAnnotatedString {
            withStyle(SpanStyle(color = MaterialTheme.colorScheme.onBackground)) {
                append("Already have an account?")
            }
            append("  ")
            withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                pushStringAnnotation(signInText, signInText)
                append(signInText)
            }
        }

        ClickableText(
            text = signInAnnotation,
            onClick = { onLoginClick() }
        )

        // Loading indicator
        if (registerState is RegisterState.Loading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(24.dp)
                    .padding(vertical = 8.dp)
            )
        }

        // Handle navigation on successful registration
        LaunchedEffect(registerState) {
            if (registerState is RegisterState.Success) {
                navHostController.navigate(Route.HomeScreen.name) {
                    popUpTo("login_flow") { inclusive = true }
                }
            } else if (registerState is RegisterState.Error) {
                snackbarHostState.showSnackbar((registerState as RegisterState.Error).message)
            }
        }
    }

    // Add SnackbarHost to display snackbars
    Box(modifier = Modifier.fillMaxSize(), Alignment.BottomCenter){
        SnackbarHost(hostState = snackbarHostState,)
    }
}

@Preview(showSystemUi = true)
@Composable
fun PrevSignUp() {
    FiloDiscussTheme {
        val navController = rememberNavController()
        SignUpScreen(
            onLoginClick = {},
            navHostController = navController,
        )
    }
}