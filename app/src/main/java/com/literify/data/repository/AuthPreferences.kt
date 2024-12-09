package com.literify.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.literify.util.KeystoreUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthPreferences @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private val loggedUser = stringPreferencesKey("logged_user")

    private fun identifierKey(identifier: String) = stringPreferencesKey("${identifier}_identifier")
    private fun passwordKey(identifier: String) = stringPreferencesKey("${identifier}_password")
    private fun credentialSavePromptedKey(identifier: String) = booleanPreferencesKey("${identifier}_is_credential_save_prompted")

    suspend fun setLoggedUser(user: String) {
        dataStore.edit { preferences ->
            preferences[loggedUser] = user
        }
    }

    suspend fun clearLoggedUser() {
        dataStore.edit { preferences ->
            preferences.remove(loggedUser)
        }
    }

    fun getCredential(): Flow<CredentialInfo?> {
        return dataStore.data.map { preferences ->
            val identifier = preferences[loggedUser] ?: return@map null

            val encryptedId = preferences[identifierKey(identifier)]
            val encryptedPassword = preferences[passwordKey(identifier)]
            val isPrompted = preferences[credentialSavePromptedKey(identifier)] ?: false

            val id = encryptedId?.let { KeystoreUtils.decrypt(it) }
            val password = encryptedPassword?.let { KeystoreUtils.decrypt(it) }

            if (id != null && password != null) {
                CredentialInfo(id, password, isPrompted)
            } else {
                null
            }
        }
    }

    suspend fun saveCredential(identifier: String, password: String, isPrompted: Boolean?) {
        val encryptedId = KeystoreUtils.encrypt(identifier)
        val encryptedPassword = KeystoreUtils.encrypt(password)

        dataStore.edit { preferences ->
            preferences[identifierKey(identifier)] = encryptedId
            preferences[passwordKey(identifier)] = encryptedPassword
            isPrompted?.let {
                preferences[credentialSavePromptedKey(identifier)] = it
            }
        }
    }

    suspend fun clearCredential(identifier: String) {
        dataStore.edit { preferences ->
            preferences.remove(identifierKey(identifier))
            preferences.remove(passwordKey(identifier))
            preferences.remove(credentialSavePromptedKey(identifier))
        }
    }
}

data class CredentialInfo(
    val id: String?,
    val password: String?,
    val isCredentialSavePrompted: Boolean
)
