package com.muhasib.documate.utils

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object NotificationPreferenceHelper {
    private val Context.dataStore by preferencesDataStore(name = "notification_preferences")

    private val NOTIFICATION_KEY = booleanPreferencesKey("notification_enabled")

    fun getNotificationStatus(context: Context): Boolean {
        return runBlocking {
            try {
                context.dataStore.data.first()[NOTIFICATION_KEY] ?: true
            } catch (e: Exception) {
                true
            }
        }
    }
}