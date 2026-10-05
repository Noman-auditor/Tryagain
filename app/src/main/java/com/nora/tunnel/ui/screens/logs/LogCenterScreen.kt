package com.nora.tunnel.ui.screens.logs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nora.tunnel.data.log.LogRedactor

@Composable
fun LogCenterScreen() {
    val logs = remember { listOf("Connection started", "password=secret123 connected successfully", "DNS query sent") }
    
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Log Center", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(logs) { line ->
                Text(
                    text = LogRedactor.redact(line),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp,
                    color = Color(0xFF8B92A8),
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { }, modifier = Modifier.weight(1f)) { Text("Pause") }
            Button(onClick = { }, modifier = Modifier.weight(1f)) { Text("Export Log") }
        }
    }
}
