package com.literify.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthPreferences @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private val credentialKey = stringPreferencesKey("credential")
    private val isCredentialSavePrompted = booleanPreferencesKey("is_credential_save_prompted")

    fun getCredential(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[credentialKey]
        }
    }

    suspend fun saveCredential(id: String, password: String) {
        dataStore.edit { preferences ->
            preferences[credentialKey] = "$id:$password"
        }
    }

    suspend fun clearCredential() {
        dataStore.edit { preferences ->
            preferences.remove(credentialKey)
        }
    }

    fun isCredentialSavePrompted(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[isCredentialSavePrompted] ?: false
        }
    }

    suspend fun setCredentialSavePrompted(bool: Boolean = true) {
        dataStore.edit { preferences ->
            preferences[isCredentialSavePrompted] = bool
        }
    }
}