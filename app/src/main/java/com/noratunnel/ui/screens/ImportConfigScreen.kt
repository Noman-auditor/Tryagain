package com.noratunnel.ui.screens
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.noratunnel.data.db.ServerProfileDao
import com.noratunnel.data.db.ServerProfileEntity
import com.noratunnel.domain.usecase.ValidateConfigUseCase
import com.noratunnel.security.KeystoreHelper
import com.noratunnel.vpn.ConfigParser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ImportVm @Inject constructor(private val dao: ServerProfileDao, private val keystore: KeystoreHelper, private val validator: ValidateConfigUseCase): ViewModel(){
    suspend fun import(content:String, name:String){ 
        validator.validate(content).getOrThrow()
        val p = ConfigParser.parse(content, name, keystore)
        dao.upsert(ServerProfileEntity(name=p.name, endpoint=p.endpoint, serverPublicKey=p.serverPublicKey, encryptedPrivateKeyAlias=p.encryptedPrivateKeyAlias, clientAddress=p.clientAddress, clientIpv6=p.clientIpv6, dns=p.dns, allowedIps=p.allowedIps, keepalive=p.persistentKeepalive, mtu=p.mtu))
    }
}

@Composable
fun ImportConfigScreen(nav: NavController, vm: ImportVm = androidx.hilt.navigation.compose.hiltViewModel()){
    val ctx = LocalContext.current
    var text by remember{ mutableStateOf("")}
    var error by remember{ mutableStateOf<String?>(null)}
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()){ uri: Uri? ->
        uri?.let{ text = ctx.contentResolver.openInputStream(it)?.bufferedReader()?.readText() ?: "" }
    }
    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)){
        Text("Import .wg / .conf", style=MaterialTheme.typography.headlineSmall)
        Text("Uses Storage Access Framework. File is validated locally and never uploaded.", style=MaterialTheme.typography.bodySmall)
        Button(onClick = { launcher.launch("*/*")}){ Text("Select File") }
        OutlinedTextField(text,{text=it}, modifier=Modifier.fillMaxWidth().height(220.dp), placeholder={Text("[Interface]\nPrivateKey=...\nAddress=...\n[Peer]\nPublicKey=...\nEndpoint=...")})
        error?.let{ Text(it, color=MaterialTheme.colorScheme.error)}
        Button(onClick = {
            try{ kotlinx.coroutines.runBlocking{ vm.import(text, "Imported") }; nav.popBackStack() } catch(e:Exception){ error=e.message }
        }, modifier=Modifier.fillMaxWidth()){ Text("Validate & Save") }
        Text("Validates: Interface, PrivateKey, Address, Peer, PublicKey, Endpoint, AllowedIPs. Rejects malformed.", style=MaterialTheme.typography.labelSmall)
    }
}
