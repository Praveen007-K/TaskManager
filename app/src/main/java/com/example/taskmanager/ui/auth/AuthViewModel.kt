package com.example.taskmanager.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanager.data.local.PreferencesDataStore
import com.example.taskmanager.data.model.User
import com.example.taskmanager.data.remote.AuthDataSource
import com.example.taskmanager.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authDataSource: AuthDataSource,
    private val userRepository: UserRepository,
    private val preferencesDataStore: PreferencesDataStore
) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            try {
                val firebaseUser = authDataSource.signInWithGoogle(idToken)
                val user = User(
                    uid = firebaseUser.uid,
                    displayName = firebaseUser.displayName ?: "",
                    email = firebaseUser.email ?: "",
                    photoURL = firebaseUser.photoUrl?.toString() ?: ""
                )
                userRepository.saveUserToFirestore(user)
                _uiState.value = AuthUiState.Success(user)
            } catch (e: Exception) {
                _uiState.value = AuthUiState.Error(e.message ?: "Sign in failed")
            }
        }
    }

    fun signOut() {
        authDataSource.signOut()
        _uiState.value = AuthUiState.Idle
    }

    fun isLoggedIn(): Boolean = userRepository.isLoggedIn()

    fun markOnboardingSeen() {
        viewModelScope.launch {
            preferencesDataStore.setOnboardingSeen()
        }
    }

    fun resetState() {
        _uiState.value = AuthUiState.Idle
    }
}
