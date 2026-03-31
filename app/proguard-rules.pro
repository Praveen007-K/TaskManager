# Add project specific ProGuard rules here.

# ── Kotlin ──────────────────────────────────────────────────────────────────
-keep class kotlin.Metadata { *; }

# ── Hilt ────────────────────────────────────────────────────────────────────
-keepclassmembers,allowobfuscation class * {
    @javax.inject.Inject <init>(...);
    @javax.inject.Inject <fields>;
}

# ── Room ─────────────────────────────────────────────────────────────────────
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-keep @androidx.room.Dao class *

# ── Firebase ─────────────────────────────────────────────────────────────────
-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.** { *; }

# ── Data classes (Firestore serialisation) ───────────────────────────────────
-keep class com.taskflow.data.model.** { *; }

# ── Coroutines ───────────────────────────────────────────────────────────────
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
