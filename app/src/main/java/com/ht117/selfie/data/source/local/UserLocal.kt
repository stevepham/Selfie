package com.ht117.selfie.data.source.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class UserLocal(private val context: Context) {
    private val isLoggedInKey = booleanPreferencesKey("user_logged_in")

    fun isUserLoggedIn() = context.dataStore.data.map {
        it[isLoggedInKey]?: false
    }

    suspend fun setLoggedIn() = context.dataStore.edit {
        it[isLoggedInKey] = true
    }

    suspend fun logout() = context.dataStore.edit {
        it[isLoggedInKey] = false
    }
}
