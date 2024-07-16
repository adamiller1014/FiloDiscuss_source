package com.example.filodiscuss.cors.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.filodiscuss.cors.components.LoadingScreen
import com.example.filodiscuss.features.auth.presentation.AuthViewModel
import com.example.filodiscuss.features.auth.presentation.screen.LoginScreen
import com.example.filodiscuss.features.auth.presentation.screen.SignUpScreen
import com.example.filodiscuss.features.home.presentation.screen.HomeScreen
import com.example.filodiscuss.features.home.presentation.screen.PostFormScreen

sealed class Route(val name: String) {
    data object LoginScreen : Route("Login")
    data object SignUpScreen : Route("SignUp")
    data object HomeScreen : Route("Home")
    data object LoadingScreen : Route("Loading")
    data object PostFormScreen : Route("PostForm")

}

@Composable
fun Navigation(navHostController: NavHostController) {
    val authViewModel: AuthViewModel = hiltViewModel()
    val checkCookieValidity by authViewModel.checkCookieValidity.collectAsState()
    val logoutState by authViewModel.logoutState.collectAsState()

    LaunchedEffect(Unit) {
        authViewModel.checkCookieValidity()
    }

    LaunchedEffect(checkCookieValidity) {
        when (checkCookieValidity) {
            true -> {
                navHostController.navigate(Route.HomeScreen.name) {
                    popUpTo(navHostController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
            false -> {
                navHostController.navigate(Route.LoginScreen.name) {
                    popUpTo(navHostController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
            null -> {}
        }
    }

    LaunchedEffect(logoutState) {
        if (logoutState) {
            navHostController.navigate(Route.LoginScreen.name) {
                popUpTo(navHostController.graph.findStartDestination().id) {
                    inclusive = true
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
            authViewModel.resetLogoutState()
        }
    }


    NavHost(
        navController = navHostController,
        startDestination = Route.LoadingScreen.name
    ) {
        composable(Route.LoadingScreen.name) {
            LoadingScreen()
        }
        composable(Route.LoginScreen.name) {
            LoginScreen(
                onSignUpClick = {
                    navHostController.navigate(Route.SignUpScreen.name)
                },
                navHostController = navHostController,
            )
        }
        composable(Route.SignUpScreen.name) {
            SignUpScreen(
                onLoginClick = {
                    navHostController.navigate(Route.LoginScreen.name)
                },
                navHostController = navHostController,
            )
        }
        composable(Route.HomeScreen.name) {
            HomeScreen(
                onLogoutClick = {
                    authViewModel.logout()
                },
                navHostController = navHostController
            )
        }
        composable(Route.PostFormScreen.name) {
            PostFormScreen(
                navHostController = navHostController,
                onCancel = {
                    navHostController.navigateUp()
                }
            )
        }
    }
}
