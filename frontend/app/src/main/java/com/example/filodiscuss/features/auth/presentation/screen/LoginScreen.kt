package com.example.filodiscuss.features.auth.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import com.example.filodiscuss.features.auth.presentation.screen.components.HeaderText
import com.example.filodiscuss.features.auth.presentation.screen.components.LoginTextField
import com.example.filodiscuss.ui.theme.FiloDiscussTheme

val defaultPadding = 16.dp
val itemSpacing = 8.dp

@Composable
fun LoginScreen(onLoginClick: () -> Unit, onSignUpClick: () -> Unit) {
    val (userName, setUsername) = rememberSaveable {
        mutableStateOf("")
    }
    val (password, setPassword) = rememberSaveable {
        mutableStateOf("")
    }
    val (checked, onCheckedChange) = rememberSaveable {
        mutableStateOf(false)
    }
    val isFieldsEmpty = userName.isNotEmpty() && password.isNotEmpty()


    Column(
        modifier = Modifier.fillMaxSize()
            .padding(defaultPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeaderText(
            text = "Login",
            modifier = Modifier.padding(vertical = defaultPadding)
                .align(alignment = Alignment.Start)
        )
        LoginTextField(
            value = userName,
            onValueChange = setUsername,
            labelText = "Username",
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
        Spacer(Modifier.height(itemSpacing))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(checked = checked, onCheckedChange = onCheckedChange)
                Text("Remember me")
            }
            TextButton(onClick = {}) {
                Text("Forgot Password?")
            }
        }
        Spacer(Modifier.height(itemSpacing))
        Button(
            onClick = onLoginClick,
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


    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showSystemUi = true)
@Composable
fun PrevLoginScreen() {
    FiloDiscussTheme {
        Greeting("Android")
    }
}