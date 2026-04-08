package com.example.taskmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.taskmanager.data.local.PreferencesDataStore
import com.example.taskmanager.ui.NavGraph
import com.example.taskmanager.ui.Routes
import com.example.taskmanager.ui.auth.AuthViewModel
import com.example.taskmanager.ui.theme.TaskManagerTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.activity.viewModels
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var preferencesDataStore: PreferencesDataStore

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TaskManagerTheme {
                val isOnboardingSeen by preferencesDataStore
                    .isOnboardingSeen()
                    .collectAsState(initial = null)

                when (val seen = isOnboardingSeen) {
                    null -> {
                        // Loading — wait for DataStore
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    else -> {
                        val startDestination = when {
                            !seen -> Routes.ONBOARDING
                            authViewModel.isLoggedIn() -> Routes.HOME
                            else -> Routes.SIGNIN
                        }
                        NavGraph(startDestination = startDestination)
                    }
                }
            }
        }
    }
}