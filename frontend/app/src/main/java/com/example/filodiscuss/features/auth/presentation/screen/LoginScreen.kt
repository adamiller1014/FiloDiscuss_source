package com.example.filodiscuss.features.auth.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
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
import com.example.filodiscuss.features.auth.presentation.state.LoginState
import com.example.filodiscuss.ui.theme.FiloDiscussTheme

val defaultPadding = 16.dp
val itemSpacing = 8.dp

@Composable
fun LoginScreen(
    onSignUpClick: () -> Unit,
    navHostController: NavHostController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val loginState by authViewModel.loginState.collectAsStateWithLifecycle()
    val (usernameOrEmail, setUsernameOrEmail) = rememberSaveable { mutableStateOf("") }
    val (password, setPassword) = rememberSaveable { mutableStateOf("") }
    val isFieldsEmpty = usernameOrEmail.isNotEmpty() && password.isNotEmpty()
    val snackbarHostState = remember { SnackbarHostState() }


    Column(
        modifier = Modifier.fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(defaultPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeaderText(
            text = "Login",
            modifier = Modifier.padding(vertical = defaultPadding)
                .align(alignment = Alignment.Start)
        )
        LoginTextField(
            value = usernameOrEmail,
            onValueChange = setUsernameOrEmail,
            labelText = "Username Or Email",
            leadingIcon = Icons.Default.Person,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(itemSpacing))
        LoginTextField(
            value = password,
            onValueChange = setPassword,
            labelText = "Password",
            leadingIcon = Icons.Default.Lock,
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Password,
            visualTransformation = PasswordVisualTransformation()
        )
//        Spacer(Modifier.height(itemSpacing))
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            TextButton(onClick = {}) {
//                Text("Forgot Password?")
//            }
//        }
        Spacer(Modifier.height(itemSpacing))
        Button(
            onClick = {
                authViewModel.login(usernameOrEmail, password)
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = isFieldsEmpty
        ) {
            Text("Login")
        }
        Spacer(Modifier.height(itemSpacing))

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Don't have an Account?")
            Spacer(Modifier.height(itemSpacing))
            TextButton(onClick = onSignUpClick) {
                Text("Sign Up")
            }
        }

        // Loading indicator
        if (loginState is LoginState.Loading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(24.dp)
                    .padding(vertical = 8.dp)
            )
        }

        // Handle navigation on successful login
        LaunchedEffect(loginState) {
            if (loginState is LoginState.Success) {
                navHostController.navigate(Route.HomeScreen.name) {
                    popUpTo("login_flow") { inclusive = true }
                }
            } else if (loginState is LoginState.Error) {
                snackbarHostState.showSnackbar((loginState as LoginState.Error).message)
            }
        }

        // Add SnackbarHost to display snackbars
        Box(modifier = Modifier.fillMaxSize(), Alignment.BottomCenter){
            SnackbarHost(hostState = snackbarHostState,)
        }

    }
}

@Preview(showSystemUi = true)
@Composable
fun PrevLoginScreen() {
    FiloDiscussTheme {
        val navController = rememberNavController()
        LoginScreen(
            onSignUpClick = {},
            navHostController = navController,
        )
    }
}