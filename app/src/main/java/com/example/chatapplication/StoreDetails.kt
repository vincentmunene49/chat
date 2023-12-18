package com.example.chatapplication

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber

const val PASSWORD = "passwordKey"
const val JID = "jidKey"
//write to preferences datastore

suspend fun saveUserDetails(context: Context, jid: String, password: String) {
    val passwordKey = PreferencesKeys.password
    val jidKey = PreferencesKeys.jid
    context.datastore.edit {
        it[passwordKey] = password
        it[jidKey] = jid
    }
    //Timber.tag("saveUserDetails").d("jid: $jid, password: $password")
}


//Read preferences

fun readUserDetails(context: Context): Flow<Pair<String, String>> = context.datastore.data.map {
    val password = it[PreferencesKeys.password] ?: ""
    val jid = it[PreferencesKeys.jid] ?: ""
    Pair(jid, password)
}

private val Context.datastore: DataStore<Preferences> by preferencesDataStore(
    name = "user details"
)

private object PreferencesKeys {
    val password = stringPreferencesKey(PASSWORD)
    val jid = stringPreferencesKey(JID)
}