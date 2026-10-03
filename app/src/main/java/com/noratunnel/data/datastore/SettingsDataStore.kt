package com.noratunnel.data.datastore
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.map
class SettingsDataStore(val ds: DataStore<Preferences>) {
    companion object {
        val AUTO_CONNECT = booleanPreferencesKey("auto_connect")
        val DNS_MODE = stringPreferencesKey("dns_mode")
        val IPV6_MODE = stringPreferencesKey("ipv6_mode")
        val SELECTED_SERVER = longPreferencesKey("selected_server")
    }
    val autoConnect = ds.data.map { it[AUTO_CONNECT] ?: false }
    val dnsMode = ds.data.map { it[DNS_MODE] ?: "vpn" }
    suspend fun setSelected(id: Long){ ds.edit{ it[SELECTED_SERVER]=id } }
    suspend fun setAuto(v: Boolean){ ds.edit{ it[AUTO_CONNECT]=v } }
}
