package com.peanut.nas.compose.model

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.peanut.nas.compose.data.Configuration
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class ConfigurationDataStore(private val context: Context) {

    companion object{
        private const val TAG = "Configuration"
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(TAG)
        val host = stringPreferencesKey("host")
        val userName = stringPreferencesKey("userName")
        val userPassword = stringPreferencesKey("userPassword")
    }

    val getConfiguration: Flow<Configuration?> = context.dataStore.data
        .map { value: Preferences ->
            Configuration(host = value[host]?:"",
            userName = value[userName]?:"",
            userPassword = value[userPassword]?:"")
        }

    suspend fun saveConfiguration(configuration: Configuration){
        context.dataStore.edit { mutablePreferences ->
            mutablePreferences[host] = configuration.host
            mutablePreferences[userName] = configuration.userName
            mutablePreferences[userPassword] = configuration.userPassword
        }
    }
}