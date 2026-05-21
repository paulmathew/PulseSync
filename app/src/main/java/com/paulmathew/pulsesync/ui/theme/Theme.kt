package com.paulmathew.pulsesync.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf

private val PulseDarkColorScheme = darkColorScheme(
    primary = PulseColors.AccentPrimary,
    secondary = PulseColors.AccentSecondary,
    tertiary = PulseColors.TrustGreen,
    error = PulseColors.FailureRed,
    background = PulseColors.BackgroundPrimary,
    surface = PulseColors.SurfacePrimary,
    surfaceVariant = PulseColors.SurfaceSecondary,
    outline = PulseColors.BorderSubtle,
    onPrimary = PulseColors.TextPrimary,
    onSecondary = PulseColors.TextPrimary,
    onTertiary = PulseColors.BackgroundPrimary,
    onError = PulseColors.TextPrimary,
    onBackground = PulseColors.TextPrimary,
    onSurface = PulseColors.TextPrimary,
    onSurfaceVariant = PulseColors.TextSecondary
)

val LocalPulseSpacing = staticCompositionLocalOf { PulseSpace }
val LocalPulseRadii = staticCompositionLocalOf { PulseRadius }
val LocalPulseMotion = staticCompositionLocalOf { PulseMotionDefaults }

@Composable
fun PulseTheme(
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalPulseSpacing provides PulseSpace,
        LocalPulseRadii provides PulseRadius,
        LocalPulseMotion provides PulseMotionDefaults
    ) {
        MaterialTheme(
            colorScheme = PulseDarkColorScheme,
            typography = PulseTypography,
            shapes = PulseShapes,
            content = content
        )
    }
}

@Composable
fun PulseSyncTheme(
    content: @Composable () -> Unit
) {
    PulseTheme(content = content)
}

object PulseThemeTokens {
    val colors: PulseColors
        @Composable
        @ReadOnlyComposable
        get() = PulseColors

    val spacing: PulseSpacing
        @Composable
        @ReadOnlyComposable
        get() = LocalPulseSpacing.current

    val radii: PulseRadii
        @Composable
        @ReadOnlyComposable
        get() = LocalPulseRadii.current

    val motion: PulseMotion
        @Composable
        @ReadOnlyComposable
        get() = LocalPulseMotion.current
}
