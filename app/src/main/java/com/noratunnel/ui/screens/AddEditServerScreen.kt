package com.noratunnel.ui.screens
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.noratunnel.data.db.ServerProfileDao
import com.noratunnel.data.db.ServerProfileEntity
import com.noratunnel.security.KeystoreHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditViewModel @Inject constructor(private val dao: ServerProfileDao, private val keystore: KeystoreHelper): ViewModel(){
    suspend fun save(name:String, endpoint:String, pub:String, priv:String, address:String, dns:String, allowed:String, keepalive:Int, existingId: Long?): Long {
        val alias = if(priv.isNotBlank()) keystore.encryptAndStore(priv) else dao.getById(existingId ?: 0L)?.encryptedPrivateKeyAlias ?: error("PrivateKey required")
        val e = ServerProfileEntity(
            id = existingId ?: 0L, name=name, endpoint=endpoint, serverPublicKey=pub, 
            encryptedPrivateKeyAlias=alias, clientAddress=address, 
            clientIpv6= if(address.contains(",")) address.split(",").find{it.contains(":")}?.trim() else null, 
            dns=dns, allowedIps=allowed, keepalive=keepalive, mtu=1280
        )
        return dao.upsert(e)
    }
    suspend fun get(id:Long) = dao.getById(id)
}

@Composable
fun AddEditServerScreen(nav: NavController, id: Long?, vm: AddEditViewModel = androidx.hilt.navigation.compose.hiltViewModel()){
    var name by remember{ mutableStateOf("")}
    var endpoint by remember{ mutableStateOf("")}
    var pub by remember{ mutableStateOf("")}
    var priv by remember{ mutableStateOf("")}
    var address by remember{ mutableStateOf("10.8.0.2/32")}
    var dns by remember{ mutableStateOf("10.8.0.1")}
    var allowed by remember{ mutableStateOf("0.0.0.0/0, ::/0")}
    var keep by remember{ mutableStateOf("25")}
    val scope = rememberCoroutineScope()
    LaunchedEffect(id){ id?.let{ vm.get(it)?.let{ e-> name=e.name; endpoint=e.endpoint; pub=e.serverPublicKey; address=e.clientAddress; dns=e.dns; allowed=e.allowedIps; keep=e.keepalive.toString() }}}
    Column(Modifier.verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)){
        Text(if(id==null) "Add Server" else "Edit Server", style=MaterialTheme.typography.headlineSmall)
        OutlinedTextField(name,{name=it}, label={Text("Name (e.g. 🇸🇬 Singapore)")}, modifier=Modifier.fillMaxWidth())
        OutlinedTextField(endpoint,{endpoint=it}, label={Text("Hostname/IP:Port (1.2.3.4:51820)")}, modifier=Modifier.fillMaxWidth())
        OutlinedTextField(pub,{pub=it}, label={Text("Server PublicKey")}, modifier=Modifier.fillMaxWidth())
        OutlinedTextField(priv,{priv=it}, label={Text("Client PrivateKey (encrypted with Keystore)")}, modifier=Modifier.fillMaxWidth())
        OutlinedTextField(address,{address=it}, label={Text("Client Address (10.8.0.2/32)")}, modifier=Modifier.fillMaxWidth())
        OutlinedTextField(dns,{dns=it}, label={Text("DNS (10.8.0.1,1.1.1.1)")}, modifier=Modifier.fillMaxWidth())
        OutlinedTextField(allowed,{allowed=it}, label={Text("AllowedIPs (0.0.0.0/0 for full tunnel)")}, modifier=Modifier.fillMaxWidth())
        OutlinedTextField(keep,{keep=it}, label={Text("PersistentKeepalive")}, modifier=Modifier.fillMaxWidth())
        Text("Private key is stored encrypted via Android Keystore (AES256-GCM). Never committed to Git or logs.", style=MaterialTheme.typography.labelSmall)
        Button(onClick = { scope.launch{ vm.save(name,endpoint,pub,priv,address,dns,allowed, keep.toIntOrNull()?:25, id); nav.popBackStack() } }, modifier=Modifier.fillMaxWidth()){ Text("Save") }
    }
}
