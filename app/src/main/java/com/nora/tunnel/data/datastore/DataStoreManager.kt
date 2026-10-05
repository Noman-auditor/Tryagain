package com.nora.tunnel.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "nora_prefs")

class DataStoreManager(private val context: Context) {
    val dnsFlow: Flow<String> = context.dataStore.data.map { it[DNS_KEY] ?: "1.1.1.1" }
    val routingModeFlow: Flow<String> = context.dataStore.data.map { it[ROUTING_MODE] ?: "ALL" }
    
    suspend fun setDns(dns: String) { context.dataStore.edit { it[DNS_KEY] = dns } }
    suspend fun getDns(): String = dnsFlow.first()
    
    companion object {
        val DNS_KEY = stringPreferencesKey("dns")
        val ROUTING_MODE = stringPreferencesKey("routing_mode")
    }
}
