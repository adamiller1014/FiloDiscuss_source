package com.example.filodiscuss.cors.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.filodiscuss.features.home.presentation.screen.HomeScreen
import com.example.filodiscuss.features.auth.presentation.screen.LoginScreen
import com.example.filodiscuss.features.auth.presentation.screen.SignUpScreen

sealed class Route(val name: String) {
    data object LoginScreen : Route("Login")
    data object SignUpScreen : Route("SignUp")
    data object HomeScreen : Route("Home")
}

@Composable
fun Navigation(navHostController: NavHostController) {
    NavHost(
        navController = navHostController,
        startDestination = "login_flow",
    ) {
        navigation(startDestination = Route.LoginScreen.name, route = "login_flow") {
            composable(route = Route.LoginScreen.name) {
                LoginScreen(
                    onSignUpClick = {
                        navHostController.navigateToSingleTop(Route.SignUpScreen.name)
                    },
                    navHostController = navHostController,
                )
            }
            composable(route = Route.SignUpScreen.name) {
                SignUpScreen(
                    onLoginClick = {
                        navHostController.navigateToSingleTop(Route.LoginScreen.name)
                    },
                    navHostController = navHostController,
                )
            }
        }
        composable(route = Route.HomeScreen.name) {
            HomeScreen()
        }
    }
}

fun NavController.navigateToSingleTop(route: String) {
    navigate(route) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}