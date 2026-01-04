package com.example.myapplicationdatastore

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

// 2) Definice DataStore pro celou aplikaci
val Context.settingsDataStore by preferencesDataStore("settings")
