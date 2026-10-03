package com.noratunnel.ui.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noratunnel.data.datastore.SettingsDataStore
import com.noratunnel.data.db.ServerProfileDao
import com.noratunnel.data.db.ServerProfileEntity
import com.noratunnel.data.repository.VpnRepository
import com.noratunnel.domain.model.ServerProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dao: ServerProfileDao,
    private val repo: VpnRepository,
    private val settings: SettingsDataStore
) : ViewModel() {
    val vpnStatus = repo.status
    val logs = repo.logs
    val servers: StateFlow<List<ServerProfileEntity>> = dao.observeAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    val selectedId = settings.ds.data.map { it[SettingsDataStore.SELECTED_SERVER] }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun connect(profile: ServerProfile, activity: com.noratunnel.MainActivity) {
        viewModelScope.launch {
            settings.setSelected(profile.id)
            repo.log("VPN permission requested for ${profile.name}")
            activity.requestVpnPermission(profile)
        }
    }
    fun disconnect(activity: com.noratunnel.MainActivity) {
        repo.log("User requested disconnect")
        activity.stopVpn()
    }
    fun getProfileEntityToModel(e: ServerProfileEntity): ServerProfile = ServerProfile(
        id = e.id, name = e.name, endpoint = e.endpoint, serverPublicKey = e.serverPublicKey,
        encryptedPrivateKeyAlias = e.encryptedPrivateKeyAlias, clientAddress = e.clientAddress,
        clientIpv6 = e.clientIpv6, dns = e.dns, allowedIps = e.allowedIps, 
        persistentKeepalive = e.keepalive, mtu = e.mtu
    )
}
