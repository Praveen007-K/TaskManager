package com.example.taskmanager.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferencesDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        val ONBOARDING_SEEN = booleanPreferencesKey("onboarding_seen")
    }

    fun isOnboardingSeen(): Flow<Boolean> =
        dataStore.data.map { prefs -> prefs[ONBOARDING_SEEN] ?: false }

    suspend fun setOnboardingSeen() {
        dataStore.edit { prefs -> prefs[ONBOARDING_SEEN] = true }
    }
}
