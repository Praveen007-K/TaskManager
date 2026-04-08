package com.example.taskmanager.ui.auth

import android.app.Activity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.example.taskmanager.R
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.launch

@Composable
fun SignInScreen(
    uiState: AuthUiState,
    onSignIn: (idToken: String) -> Unit,
    onError: (String) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Show snackbar on error
    LaunchedEffect(uiState) {
        if (uiState is AuthUiState.Error) {
            snackbarHostState.showSnackbar(uiState.message)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            if (uiState is AuthUiState.Loading) {
                CircularProgressIndicator()
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // App logo / title area
                    Text(
                        text = "✅",
                        style = MaterialTheme.typography.displayLarge
                    )

                    Text(
                        text = "TaskManager",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Text(
                        text = "Sign in to sync your tasks, earn points, and stay on top of everything.",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Google Sign-In Button
                    OutlinedButton(
                        onClick = {
                            coroutineScope.launch {
                                launchGoogleSignIn(
                                    activity = context as Activity,
                                    webClientId = context.getString(R.string.default_web_client_id),
                                    onSuccess = onSignIn,
                                    onError = onError
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "G",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            )
                            Text(
                                text = "Sign in with Google",
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                }
            }
        }
    }
}

private suspend fun launchGoogleSignIn(
    activity: Activity,
    webClientId: String,
    onSuccess: (String) -> Unit,
    onError: (String) -> Unit
) {
    val credentialManager = CredentialManager.create(activity)

    // First try: show accounts already on device
    val googleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false)
        .setServerClientId(webClientId)
        .setAutoSelectEnabled(false)
        .build()

    // Fallback: full "Sign in with Google" bottom sheet (works even when no saved accounts)
    val signInWithGoogleOption = GetSignInWithGoogleOption.Builder(webClientId).build()

    suspend fun attemptSignIn(useFallback: Boolean): String {
        val request = GetCredentialRequest.Builder()
            .apply {
                if (useFallback) addCredentialOption(signInWithGoogleOption)
                else addCredentialOption(googleIdOption)
            }
            .build()
        val result = credentialManager.getCredential(context = activity, request = request)
        val credential = result.credential
        check(credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            "Unexpected credential type"
        }
        return GoogleIdTokenCredential.createFrom(credential.data).idToken
    }

    try {
        onSuccess(attemptSignIn(useFallback = false))
    } catch (e: NoCredentialException) {
        // No saved credentials — fall back to full account picker
        try {
            onSuccess(attemptSignIn(useFallback = true))
        } catch (e2: GetCredentialException) {
            onError(e2.message ?: "Sign in cancelled")
        } catch (e2: Exception) {
            onError(e2.message ?: "Sign in failed")
        }
    } catch (e: GetCredentialException) {
        onError(e.message ?: "Sign in cancelled")
    } catch (e: Exception) {
        onError(e.message ?: "Sign in failed")
    }
}
