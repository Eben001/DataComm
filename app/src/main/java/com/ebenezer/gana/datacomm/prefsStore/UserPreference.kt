package com.ebenezer.gana.datacomm.prefsStore

import android.content.Context
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(("balance"))
@Singleton
class UserPreference @Inject constructor(@ApplicationContext val context:Context) {


    val dataBalance:Flow<Double?>
    get() = context.dataStore.data.map {preferences ->
        preferences[BALANCE_KEY]
    }

    suspend fun saveDataBalance(balance:Double){
        context.dataStore.edit {preferences ->
            preferences[BALANCE_KEY] = balance
        }
    }

    companion object {
        private val BALANCE_KEY = doublePreferencesKey("balance_key")
    }
}