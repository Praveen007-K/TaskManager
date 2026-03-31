# TaskManager — Project Context

## Overview
A productivity Android application called **TaskManager** — a smart task manager with Google Calendar
integration, home screen widget support, Firebase backend, and a gamified scoring system.

## Platform & Language
- **Platform:** Android
- **Language:** Kotlin (no Java)

## Tech Stack
| Layer | Technology |
|---|---|
| UI | Jetpack Compose + Material Design 3 |
| Architecture | MVVM + Repository Pattern |
| Local DB | Room (offline cache) |
| Cloud DB | Firebase Firestore |
| Auth | Firebase Authentication (Google Sign-In) |
| Calendar | Google Calendar API (OAuth2) |
| Widget | Jetpack Glance API |
| Notifications | WorkManager + NotificationManager |
| DI | Hilt |

---

## Project Structure
```
taskflow/
├── app/src/main/java/com/taskflow/
│   ├── di/                        # Hilt modules
│   ├── data/
│   │   ├── local/                 # Room DB, DAOs, Entities
│   │   ├── remote/                # Firestore, Calendar API
│   │   ├── repository/            # TaskRepository, UserRepository
│   │   └── model/                 # Task, User, Score data classes
│   ├── domain/
│   │   └── usecase/               # Business logic (AddTask, CompleteTask, etc.)
│   ├── ui/
│   │   ├── auth/                  # SignIn screen, Onboarding
│   │   ├── home/                  # Home screen + ViewModel
│   │   ├── missed/                # Overdue screen + ViewModel
│   │   ├── history/               # History screen + ViewModel
│   │   ├── score/                 # Score/Gamification screen
│   │   ├── addtask/               # Add Task bottom sheet + ViewModel
│   │   ├── detail/                # Task detail screen
│   │   ├── settings/              # Settings screen
│   │   └── theme/                 # MaterialTheme, Colors, Typography
│   ├── widget/                    # Glance API widget
│   ├── worker/                    # WorkManager jobs
│   └── MainActivity.kt
└── build.gradle.kts
```

---

## Data Model

### Firestore: `users/{uid}/tasks/{taskId}`
```kotlin
data class Task(
    val taskId: String,           // auto-generated
    val title: String,            // required, max 100 chars
    val description: String?,     // optional, max 500 chars
    val deadline: Timestamp,      // required, must be future
    val priority: Priority,       // LOW / MEDIUM / HIGH / CRITICAL
    val status: Status,           // PENDING / DONE / OVERDUE
    val createdAt: Timestamp,
    val completedAt: Timestamp?,  // null if not done
    val googleCalendarEventId: String?, // linked calendar event
    val score: Int                // awarded on completion
)
```

### Firestore: `users/{uid}` (User Profile)
```kotlin
data class User(
    val uid: String,
    val displayName: String,
    val email: String,
    val photoURL: String
)
```

### Firestore: `users/{uid}/score` and `users/{uid}/scoreHistory`

---

## Scoring Logic
| Action | Points |
|---|---|
| Complete on time — Low | +10 |
| Complete on time — Medium | +20 |
| Complete on time — High | +35 |
| Complete on time — Critical | +50 |
| Completed late (any priority) | 50% of above |
| Task deleted before done | 0 |
| Daily streak (all today's tasks done) | +25 bonus |

---

## Priority Color Coding
| Priority | Color |
|---|---|
| Low | Green |
| Medium | Yellow / Amber |
| High | Orange |
| Critical | Red |

---

## Screens & Navigation

Bottom Navigation Bar with 4 tabs:
| Tab | Icon | Screen |
|---|---|---|
| Home | 🏠 | Today / Upcoming tasks |
| Missed | ⚠️ | Overdue tasks |
| History | 📋 | Completed tasks |
| Score | ⭐ | Gamification screen |

Settings screen accessible from profile avatar in top bar.

---

## Architecture Rules
- Strictly follow MVVM + Repository pattern
- No business logic in Composables — only in ViewModels or UseCases
- UseCases are single-responsibility (one action per class)
- Repository abstracts both Room and Firestore data sources
- Use StateFlow + UiState sealed classes for all screen state management

---

## Code Rules
- Kotlin only — no Java anywhere
- Use StateFlow / sealed UiState classes (Loading / Success / Error)
- All Composables must be stateless where possible — hoist state up
- Use Hilt for ALL dependency injection — no manual instantiation
- Handle Loading / Error / Success states explicitly on every screen
- No hardcoded strings — use strings.xml
- No hardcoded dimensions — use spacing tokens or MaterialTheme

---

## UI Rules
- Material Design 3 components only
- Full dark mode support — never hardcode colors, always use MaterialTheme.colorScheme
- Minimum tap target: 48dp on all interactive elements
- Use skeleton loaders on all list screens (not progress spinners)
- Haptic feedback on task completion swipe and score increment
- Empty state illustration + CTA when a list has no items

---

## Session Rules
- Build one screen or one feature at a time — do not jump ahead
- Show the full folder path for every new file created
- Ask before modifying any existing file
- Never silently delete or refactor existing code
- After completing each feature, write a short summary:
  - What was built
  - Files created / modified
  - What comes next

---

## Suggested Build Order
| Session | Feature |
|---|---|
| 1 | Project setup — Hilt, Room, Firestore, build.gradle config |
| 2 | Auth — Google Sign-In + Onboarding (3 slides) |
| 3 | Add Task bottom sheet + Firestore write + validation |
| 4 | Home screen — task list, filter tabs, swipe actions |
| 5 | Overdue screen + WorkManager background job |
| 6 | Score system + Score screen + level/rank logic |
| 7 | History screen — timeline view, filter/sort |
| 8 | Notifications — WorkManager reminders + overdue alerts |
| 9 | Home screen widget — Glance API (small + medium) |
| 10 | Google Calendar integration — create/update/delete events |
| 11 | Settings screen + polish + accessibility pass |

