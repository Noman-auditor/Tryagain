package com.nora.tunnel.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val NoraDarkScheme = darkColorScheme(
    primary = Color(0xFF7C4DFF),
    secondary = Color(0xFF00E5FF),
    background = Color(0xFF0A0E1A),
    surface = Color(0xFF141927),
    surfaceVariant = Color(0xFF1E2436)
)

@Composable
fun NoraTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = NoraDarkScheme, typography = Typography(), content = content)
}

@Composable
fun NoraGlassCard(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF141927).copy(alpha = 0.7f)),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.08f)),
        elevation = CardDefaults.cardElevation(8.dp)
    ) { Column(Modifier.padding(20.dp), content = content) }
}
