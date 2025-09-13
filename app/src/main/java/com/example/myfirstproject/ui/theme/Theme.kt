package com.example.myfirstproject.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
//    primary = Color(0xFF1EB980),
//    secondary = Color(0xFF045D56),
//    tertiary = Color(0xFF03DAC5)
)

private val LightColorScheme = lightColorScheme(
//    primary = Color(0xFF1EB980),
//    secondary = Color(0xFF045D56),
//    tertiary = Color(0xFF03DAC5)
)

@Composable
fun ScientificCalculatorTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
