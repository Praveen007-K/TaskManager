package com.example.taskmanager.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.taskmanager.ui.auth.AuthUiState
import com.example.taskmanager.ui.auth.AuthViewModel
import com.example.taskmanager.ui.auth.OnboardingScreen
import com.example.taskmanager.ui.auth.SignInScreen

object Routes {
    const val ONBOARDING = "onboarding"
    const val SIGNIN = "signin"
    const val HOME = "home"
}

@Composable
fun NavGraph(
    startDestination: String,
    navController: NavHostController = rememberNavController()
) {
    val authViewModel: AuthViewModel = hiltViewModel()
    val uiState by authViewModel.uiState.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Routes.ONBOARDING) {
            OnboardingScreen(
                onFinished = {
                    authViewModel.markOnboardingSeen()
                    navController.navigate(Routes.SIGNIN) {
                        popUpTo(Routes.ONBOARDING) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.SIGNIN) {
            SignInScreen(
                uiState = uiState,
                onSignIn = { idToken ->
                    authViewModel.signInWithGoogle(idToken)
                },
                onError = { message ->
                    // Error from credential manager (before Firebase) — set error state so Snackbar shows
                    authViewModel.setError(message)
                }
            )

            // Navigate to home on successful sign-in
            if (uiState is AuthUiState.Success) {
                navController.navigate(Routes.HOME) {
                    popUpTo(Routes.SIGNIN) { inclusive = true }
                }
            }
        }

        composable(Routes.HOME) {
            // Placeholder — replaced in Session 4
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Home — Session 4")
            }
        }
    }
}
