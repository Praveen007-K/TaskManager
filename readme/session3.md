# Session 3 — Add Task

## Session Goal

Build the Add Task bottom sheet with full form UI, input validation, and Firestore write. Users can create a task with a title, optional description, deadline (date + time picker), and priority selector.

## Current State

Session 2 complete. Auth working, user signed in, Home placeholder visible. No task creation yet.

---

## Development Steps

### Step 1 — Create TaskRepository Interface + Impl

File: `data/repository/TaskRepository.kt`

- `suspend fun addTask(task: Task)`
- `suspend fun updateTask(task: Task)`
- `suspend fun deleteTask(taskId: String)`
- `fun getTasks(): Flow<List<Task>>`
- `fun getTasksByStatus(status: Status): Flow<List<Task>>`

File: `data/repository/TaskRepositoryImpl.kt`

- Write to Firestore at `users/{uid}/tasks/{taskId}`
- Cache to Room `TaskEntity` on write
- Read from Room (offline-first)
- Sync from Firestore on init

### Step 2 — Add TaskRepository to RepositoryModule

File: `di/RepositoryModule.kt` _(modify)_

- `@Binds` `TaskRepositoryImpl` → `TaskRepository`

### Step 3 — Create AddTask UseCases

File: `domain/usecase/AddTaskUseCase.kt`

- Validates input (title not blank, deadline in future)
- Calculates `score` based on priority (from context.md scoring logic)
- Calls `TaskRepository.addTask()`

### Step 4 — Create AddTask UiState

File: `ui/addtask/AddTaskUiState.kt`

```kotlin
sealed class AddTaskUiState {
    object Idle : AddTaskUiState()
    object Loading : AddTaskUiState()
    object Success : AddTaskUiState()
    data class Error(val message: String) : AddTaskUiState()
    data class ValidationError(
        val titleError: String? = null,
        val deadlineError: String? = null
    ) : AddTaskUiState()
}
```

### Step 5 — Create AddTaskViewModel

File: `ui/addtask/AddTaskViewModel.kt`

- `@HiltViewModel`
- Holds form state: `title`, `description`, `deadline`, `priority`
- `fun onTitleChange(value: String)`
- `fun onDescriptionChange(value: String)`
- `fun onDeadlineChange(millis: Long)`
- `fun onPriorityChange(priority: Priority)`
- `fun submit()` — triggers `AddTaskUseCase`, emits `uiState`
- `fun resetState()`

### Step 6 — Create AddTaskBottomSheet

File: `ui/addtask/AddTaskBottomSheet.kt`

- Use `ModalBottomSheet` (Material 3)
- Fields:
  - Title: `OutlinedTextField` — max 100 chars, shows error if blank
  - Description: `OutlinedTextField` — optional, max 500 chars, multiline (3 lines)
  - Deadline: tapping opens `DatePickerDialog` then `TimePickerDialog`
  - Priority: `SingleChoiceSegmentedButtonRow` — LOW / MEDIUM / HIGH / CRITICAL with priority color coding
- "Add Task" `Button` at bottom — disabled during `Loading`
- Close button (X) in top-right
- Show `CircularProgressIndicator` overlay during `Loading`
- On `Success` → dismiss sheet + reset state
- On `ValidationError` → show inline field errors

### Step 7 — Priority Color Coding

File: `ui/theme/Color.kt` _(modify)_

- Add priority colors per context.md:
  - `Low` → Green
  - `Medium` → Amber
  - `High` → Orange
  - `Critical` → Red

File: `ui/addtask/PriorityColor.kt`

- `fun Priority.toColor(): Color` extension function

### Step 8 — Add FAB to Home Placeholder

File: `ui/NavGraph.kt` _(modify)_

- Add state to track bottom sheet visibility
- Add `FloatingActionButton` (➕) on Home route
- Show `AddTaskBottomSheet` when FAB tapped

### Step 9 — Add TaskRepository + UseCase to AppModule/RepositoryModule

File: `di/AppModule.kt` _(modify)_

- No new provides needed — Firestore + Room already provided

---

## Completion Checklist

- [ ] `TaskRepository.kt` (interface + impl) created
- [ ] `TaskRepositoryImpl` writes to Firestore + caches to Room
- [ ] `RepositoryModule.kt` updated — binds `TaskRepositoryImpl`
- [ ] `AddTaskUseCase.kt` created — validates + calculates score
- [ ] `AddTaskUiState.kt` sealed class created
- [ ] `AddTaskViewModel.kt` created with form state + submit
- [ ] `AddTaskBottomSheet.kt` created — full form UI
- [ ] Priority color extension created
- [ ] `Color.kt` updated with priority colors
- [ ] FAB added to Home placeholder → opens bottom sheet
- [ ] Task saved to Firestore and Room on submit
- [ ] Validation errors shown inline
- [ ] Sheet dismisses on success

---

## What Comes Next

**Session 4 — Home Screen:** Task list with filter tabs (Today / Upcoming / All), swipe to complete/delete, priority color indicators, empty state.
