package com.teamsolo.studyroom.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.teamsolo.studyroom.ui.auth.AuthViewModel
import com.teamsolo.studyroom.ui.auth.LoginScreen
import com.teamsolo.studyroom.ui.auth.RegisterScreen
import com.teamsolo.studyroom.ui.home.HomeScreen

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
}

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel = viewModel()
) {
    val isLoggedIn by authViewModel.isLoggedIn.collectAsStateWithLifecycle()

    // Rule 1: auto-login – decided once, when the graph is first created.
    val startDestination = remember {
        if (authViewModel.startsLoggedIn()) Routes.HOME else Routes.LOGIN
    }

    // Rules 2 & 3: react to auth state and clear the back stack.
    LaunchedEffect(isLoggedIn) {
        val current = navController.currentDestination?.route
        if (isLoggedIn && current != Routes.HOME) {
            navController.navigateClearingBackStack(Routes.HOME)
        } else if (!isLoggedIn && current == Routes.HOME) {
            navController.navigateClearingBackStack(Routes.LOGIN)
        }
    }

    NavHost(navController = navController, startDestination = startDestination) {
        composable(Routes.LOGIN) {
            LoginScreen(
                viewModel = authViewModel,
                onNavigateToRegister = {
                    authViewModel.resetForm()
                    navController.navigate(Routes.REGISTER) { launchSingleTop = true }
                }
            )
        }
        composable(Routes.REGISTER) {
            RegisterScreen(
                viewModel = authViewModel,
                onNavigateToLogin = {
                    authViewModel.resetForm()
                    navController.popBackStack(Routes.LOGIN, inclusive = false)
                }
            )
        }
        composable(Routes.HOME) {
            HomeScreen(
                userEmail = authViewModel.currentUserEmail,
                onLogout = authViewModel::logout
            )
        }
    }
}

private fun NavHostController.navigateClearingBackStack(route: String) {
    navigate(route) {
        popUpTo(graph.id) { inclusive = true }
        launchSingleTop = true
    }
}
