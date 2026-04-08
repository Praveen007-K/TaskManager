# Session 2 — Auth

## Session Goal

Implement Google Sign-In with Firebase Authentication, save the user profile to Firestore, and build a 3-slide onboarding flow shown only on first launch.

## Current State

Session 1 complete. Hilt, Room, Firebase configured. App launches with default Compose boilerplate. No auth, no navigation.

---

## Development Steps

### Step 1 — Add Auth Data Source

File: `data/remote/AuthDataSource.kt`

- Wrap Firebase Auth + Google Sign-In into a single class
- Functions: `signInWithGoogle(idToken)`, `signOut()`, `getCurrentUser()`
- Returns `FirebaseUser?`

### Step 2 — Create UserRepository

File: `data/repository/UserRepository.kt`

- Interface + Impl pattern
- On sign-in: save `User` to Firestore at `users/{uid}`
- `fun getCurrentUser(): FirebaseUser?`
- `suspend fun saveUserToFirestore(user: User)`
- `fun isLoggedIn(): Boolean`

### Step 3 — Add UserRepository to Hilt AppModule

File: `di/AppModule.kt` _(modify)_

- Provide `AuthDataSource`
- Bind `UserRepositoryImpl` to `UserRepository`

### Step 4 — Create Auth UiState

File: `ui/auth/AuthUiState.kt`

```kotlin
sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    data class Success(val user: User) : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}
```

### Step 5 — Create AuthViewModel

File: `ui/auth/AuthViewModel.kt`

- `@HiltViewModel`
- Exposes `uiState: StateFlow<AuthUiState>`
- `fun signInWithGoogle(idToken: String)`
- `fun signOut()`
- `fun isLoggedIn(): Boolean`

### Step 6 — Create OnboardingScreen (3 slides)

File: `ui/auth/OnboardingScreen.kt`

- Use `HorizontalPager` (Compose Pager) for 3 slides
- Slide 1: "Stay Organised" — task management intro
- Slide 2: "Never Miss a Deadline" — reminders + overdue alerts
- Slide 3: "Earn Points" — gamification scoring intro
- Each slide: full-screen illustration area + title + subtitle
- Dot indicator at bottom
- "Skip" button (top-right) on slides 1 & 2
- "Get Started" button on slide 3 → navigates to SignIn screen
- Store onboarding-seen flag in `DataStore`

### Step 7 — Create DataStore Helper

File: `data/local/PreferencesDataStore.kt`

- Use Jetpack `DataStore<Preferences>`
- `suspend fun setOnboardingSeen()`
- `fun isOnboardingSeen(): Flow<Boolean>`

### Step 8 — Add DataStore to Hilt AppModule

File: `di/AppModule.kt` _(modify)_

- Provide `DataStore<Preferences>` singleton

### Step 9 — Create SignInScreen

File: `ui/auth/SignInScreen.kt`

- Stateless Composable — accepts `uiState` + lambda callbacks
- "Sign in with Google" button — Material 3 `OutlinedButton` with Google logo
- Show `CircularProgressIndicator` during `Loading` state
- Show `Snackbar` on `Error` state
- On `Success` → navigate to Home

### Step 10 — Set Up Navigation Graph

File: `ui/NavGraph.kt`

- Use `NavHost` with `rememberNavController()`
- Routes: `onboarding`, `signin`, `home` (placeholder for now)
- Start destination logic:
  - If onboarding not seen → `onboarding`
  - If onboarding seen but not logged in → `signin`
  - If logged in → `home`

### Step 11 — Update MainActivity

File: `MainActivity.kt` _(modify)_

- Replace `Greeting` composable with `NavGraph`
- Pass `navController`

### Step 12 — Add Credential Manager dependency

File: `gradle/libs.versions.toml` + `app/build.gradle.kts` _(modify)_

- Add `androidx.credentials:credentials` + `androidx.credentials:credentials-play-services-auth`
- Add `com.google.android.libraries.identity.googleid:googleid`
- These replace the deprecated `ActivityResultLauncher` Google Sign-In flow

### Step 13 — Add WebClientId to strings.xml

File: `res/values/strings.xml` _(modify)_

- Add `<string name="default_web_client_id">YOUR_WEB_CLIENT_ID</string>`
- Value comes from `google-services.json` → `oauth_client[client_type=3].client_id`

---

## Completion Checklist

- [x] `AuthDataSource.kt` created
- [x] `UserRepository.kt` (interface + impl) created
- [x] `AuthUiState.kt` sealed class created
- [x] `AuthViewModel.kt` created with `@HiltViewModel`
- [x] `PreferencesDataStore.kt` created
- [x] `OnboardingScreen.kt` created — 3 slides with pager + dots
- [x] `SignInScreen.kt` created — Google Sign-In button + states
- [x] `NavGraph.kt` created — onboarding / signin / home routing
- [x] `MainActivity.kt` updated to host `NavGraph`
- [x] `AppModule.kt` updated — FirebaseAuth, FirebaseFirestore, DataStore providers
- [x] `RepositoryModule.kt` created — binds `UserRepositoryImpl` to `UserRepository`
- [x] `libs.versions.toml` + `app/build.gradle.kts` updated — Credential Manager deps
- [x] `strings.xml` updated — `default_web_client_id`
- [ ] Sign-in flow tested on emulator end-to-end

---

## What Comes Next

**Session 3 — Add Task:** Bottom sheet UI, form validation, Firestore write, priority/deadline picker.
