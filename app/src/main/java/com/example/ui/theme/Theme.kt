package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme =
  darkColorScheme(
    primary = ParmondPrimaryDark,
    onPrimary = Color(0xFF003554),
    primaryContainer = Color(0xFF004D74),
    onPrimaryContainer = Color(0xFFC3E7FF),
    secondary = ParmondSecondaryDark,
    onSecondary = Color(0xFF1E1E60),
    secondaryContainer = Color(0xFF33337B),
    onSecondaryContainer = Color(0xFFE0E0FF),
    tertiary = ParmondAccentCyan,
    onTertiary = Color(0xFF00363D),
    background = ParmondDarkBg,
    onBackground = Color(0xFFF1F5F9),
    surface = ParmondDarkSurface,
    onSurface = Color(0xFFF1F5F9),
    surfaceVariant = ParmondDarkSurfaceVariant,
    onSurfaceVariant = Color(0xFFCBD5E1),
    outline = Color(0xFF475569)
  )

private val LightColorScheme =
  lightColorScheme(
    primary = ParmondPrimaryLight,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFC3E7FF),
    onPrimaryContainer = Color(0xFF001E30),
    secondary = ParmondSecondary,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE0E0FF),
    onSecondaryContainer = Color(0xFF16164A),
    tertiary = Color(0xFF0891B2),
    onTertiary = Color.White,
    background = ParmondLightBg,
    onBackground = Color(0xFF0F172A),
    surface = ParmondLightSurface,
    onSurface = Color(0xFF0F172A),
    surfaceVariant = ParmondLightSurfaceVariant,
    onSurfaceVariant = Color(0xFF475569),
    outline = Color(0xFF94A3B8)
  )

@Composable
fun ParmondTechTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}

