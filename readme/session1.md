# Session 1 — Project Setup

## Session Goal

Configure `build.gradle.kts` with all dependencies, set up Hilt, Room, Firebase Firestore, Firebase Auth, and create the base folder structure as defined in `context.md`.

## Current State

Empty Android project with default Compose boilerplate. No DI, no DB, no Firebase.

---

## Development Steps

### Step 1 — Update `gradle/libs.versions.toml`

Add all required library versions and dependencies:

- Hilt (DI)
- Room (local DB)
- Firebase BOM (Firestore + Auth)
- WorkManager
- Navigation Compose
- Jetpack Glance (widget)
- Coroutines
- Lifecycle ViewModel Compose
- Google Play Services Auth (Google Sign-In)

### Step 2 — Update Root `build.gradle.kts`

Add plugin declarations (apply false):

- `com.google.dagger.hilt.android`
- `com.google.gms.google-services`
- `com.google.devtools.ksp`

### Step 3 — Update `app/build.gradle.kts`

- Apply plugins: `hilt`, `ksp`, `google-services`
- Add all dependency blocks from `libs.versions.toml`
- Enable `buildFeatures { compose = true }`
- Set KSP for Room annotation processor and Hilt

### Step 4 — Add `google-services.json`

- Place Firebase project config file at `app/google-services.json`
- Required for Firebase Auth + Firestore to initialise

### Step 5 — Create `TaskManagerApplication.kt`

- Annotate with `@HiltAndroidApp`
- Register in `AndroidManifest.xml` via `android:name`

### Step 6 — Update `AndroidManifest.xml`

- Add `android:name=".TaskManagerApplication"`
- Add internet permission: `<uses-permission android:name="android.permission.INTERNET" />`

### Step 7 — Create Base Folder Structure

Create empty package folders (with `.gitkeep` or placeholder files) matching context.md layout:

```
com/example/taskmanager/
├── di/
├── data/
│   ├── local/
│   ├── remote/
│   ├── repository/
│   └── model/
├── domain/
│   └── usecase/
├── ui/
│   ├── auth/
│   ├── home/
│   ├── missed/
│   ├── history/
│   ├── score/
│   ├── addtask/
│   ├── detail/
│   ├── settings/
│   └── theme/
├── widget/
└── worker/
```

### Step 8 — Create Data Models

File: `data/model/Task.kt`

- `enum class Priority { LOW, MEDIUM, HIGH, CRITICAL }`
- `enum class Status { PENDING, DONE, OVERDUE }`
- `data class Task(...)` with all fields from context.md

File: `data/model/User.kt`

- `data class User(uid, displayName, email, photoURL)`

### Step 9 — Set Up Room

File: `data/local/TaskEntity.kt` — Room `@Entity` mapping of `Task`
File: `data/local/TaskDao.kt` — `@Dao` with CRUD + query methods
File: `data/local/AppDatabase.kt` — `@Database` with `TaskEntity`

### Step 10 — Create Hilt AppModule

File: `di/AppModule.kt`

- Provide `AppDatabase` singleton
- Provide `TaskDao` from database
- (Firebase and Firestore instances provisioned here in later sessions)

---

## Completion Checklist

- [ ] `libs.versions.toml` updated with all deps
- [ ] Root `build.gradle.kts` updated with all plugins
- [ ] `app/build.gradle.kts` updated with all deps + plugins
- [ ] `google-services.json` placed in `app/`
- [ ] `TaskManagerApplication.kt` created with `@HiltAndroidApp`
- [ ] `AndroidManifest.xml` updated (application name + internet permission)
- [ ] Base folder structure created
- [ ] `Priority`, `Status` enums created
- [ ] `Task` data class created
- [ ] `User` data class created
- [ ] `TaskEntity` Room entity created
- [ ] `TaskDao` Room DAO created
- [ ] `AppDatabase` Room database created
- [ ] `AppModule` Hilt module created

---

## What Comes Next

**Session 2 — Auth:** Google Sign-In screen, Firebase Auth integration, 3-slide onboarding flow.
