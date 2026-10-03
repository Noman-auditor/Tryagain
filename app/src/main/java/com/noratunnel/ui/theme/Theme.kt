package com.noratunnel.ui.theme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF00E5FF), onPrimary = Color.Black,
    background = Color(0xFF0B1420), surface = Color(0xFF162033),
    surfaceVariant = Color(0xFF1E2F4A), onBackground = Color.White, onSurface = Color.White
)
private val LightColorScheme = lightColorScheme(primary = Color(0xFF0066FF), background = Color(0xFFF8F9FF), surface = Color.White)
@Composable
fun NoraTunnelTheme(darkTheme: Boolean = true, content: @Composable () -> Unit) {
    val scheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(colorScheme = scheme, typography = Typography(
        headlineLarge = androidx.compose.ui.text.TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Bold),
        titleLarge = androidx.compose.ui.text.TextStyle(fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
    ), content = content)
}
