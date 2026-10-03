package com.noratunnel.ui.screens
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.journeyapps.barcodescanner.ScanContract

@Composable
fun QrScannerScreen(nav: NavController, vm: ImportVm = hiltViewModel()){
    var result by remember{ mutableStateOf<String?>(null)}
    var error by remember{ mutableStateOf<String?>(null)}
    val launcher = rememberLauncherForActivityResult(ScanContract()){ res ->
        res.contents?.let{ result = it }
    }
    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)){
        Text("QR Import", style=MaterialTheme.typography.headlineSmall)
        Text("Scan -> Parse -> Validate -> Show summary -> User confirmation -> Encrypt -> Save. Never sent to remote server.", style=MaterialTheme.typography.bodySmall)
        Button(onClick = { launcher.launch(null) }, modifier=Modifier.fillMaxWidth()){ Text("Scan QR") }
        result?.let{
            Card(Modifier.fillMaxWidth()){ Column(Modifier.padding(12.dp)){ 
                Text("Summary", style=MaterialTheme.typography.titleMedium)
                Text(it.take(300)+"...", style=MaterialTheme.typography.bodySmall) 
            }}
            Button(onClick = {
                try{ kotlinx.coroutines.runBlocking{ vm.import(it, "QR Import") }; nav.popBackStack() } catch(e:Exception){ error=e.message }
            }, modifier=Modifier.fillMaxWidth()){ Text("Confirm & Save") }
            error?.let{ Text(it, color=MaterialTheme.colorScheme.error)}
        }
    }
}
