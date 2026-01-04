package com.example.myapp17_vanocniaplikacce

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "christmas_prefs")

class DataStoreManager(private val context: Context) {

    private val MY_GIFTS_KEY = stringSetPreferencesKey("my_gifts")

    val myGiftsFlow: Flow<Set<String>> = context.dataStore.data.map { preferences ->
        preferences[MY_GIFTS_KEY] ?: emptySet()
    }

    suspend fun addGift(giftName: String) {
        context.dataStore.edit { preferences ->
            val currentGifts = preferences[MY_GIFTS_KEY] ?: emptySet()
            preferences[MY_GIFTS_KEY] = currentGifts + giftName
        }
    }
}
