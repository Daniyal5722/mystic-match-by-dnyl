package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val NeoBrutalistColorScheme =
  lightColorScheme(
    primary = Primary,
    onPrimary = OnPrimary,
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = OnPrimaryContainer,
    secondary = Secondary,
    onSecondary = OnSecondary,
    secondaryContainer = SecondaryContainer,
    onSecondaryContainer = OnSecondaryContainer,
    tertiary = Tertiary,
    onTertiary = OnTertiary,
    tertiaryContainer = TertiaryContainer,
    onTertiaryContainer = OnTertiaryContainer,
    background = SurfaceBg,
    onBackground = OnSurface,
    surface = SurfaceBg,
    onSurface = OnSurface,
    surfaceVariant = SurfaceContainerHigh,
    onSurfaceVariant = OnSurfaceVariant,
    outline = Outline,
    outlineVariant = OutlineVariant,
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = false,
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  MaterialTheme(
    colorScheme = NeoBrutalistColorScheme,
    typography = Typography,
    content = content,
  )
}
