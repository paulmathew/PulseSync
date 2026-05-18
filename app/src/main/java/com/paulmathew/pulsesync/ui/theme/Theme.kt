package com.paulmathew.pulsesync.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val PulseDarkColorScheme = darkColorScheme(
    primary = OperationalGreen,
    secondary = InformationalBlue,
    error = FailureRed,
    background = GraphiteBackground,
    surface = PanelSurface,
    onPrimary = GraphiteBackground,
    onSecondary = TextPrimary,
    onError = TextPrimary,
    onBackground = TextPrimary,
    onSurface = TextPrimary
)

@Composable
fun PulseSyncTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = PulseDarkColorScheme,
        typography = PulseTypography,
        content = content
    )
}