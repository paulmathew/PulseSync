package com.paulmathew.pulsesync.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.paulmathew.pulsesync.ui.theme.PulseColors
import com.paulmathew.pulsesync.ui.theme.PulseThemeTokens

enum class PulseSurfaceTone {
    Base,
    Elevated,
    Muted,
    Accent
}

@Composable
fun PulseSurface(
    modifier: Modifier = Modifier,
    tone: PulseSurfaceTone = PulseSurfaceTone.Base,
    shape: Shape = PulseThemeTokens.radii.medium,
    borderColor: Color = PulseColors.BorderSubtle,
    tonalElevation: Dp = 0.dp,
    contentPadding: PaddingValues = PaddingValues(PulseThemeTokens.spacing.md),
    content: @Composable BoxScope.() -> Unit
) {
    val backgroundColor = when (tone) {
        PulseSurfaceTone.Base -> PulseColors.SurfacePrimary
        PulseSurfaceTone.Elevated -> PulseColors.SurfaceElevated
        PulseSurfaceTone.Muted -> PulseColors.SurfaceSecondary
        PulseSurfaceTone.Accent -> PulseColors.AccentSoft
    }

    Box(
        modifier = modifier
            .shadow(
                elevation = tonalElevation,
                shape = shape,
                ambientColor = Color.Black.copy(alpha = 0.22f),
                spotColor = Color.Black.copy(alpha = 0.30f)
            )
            .background(
                color = backgroundColor,
                shape = shape
            )
            .border(
                border = BorderStroke(1.dp, borderColor),
                shape = shape
            )
            .padding(contentPadding),
        content = content
    )
}

@Composable
fun PulseCard(
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    contentPadding: PaddingValues = PaddingValues(PulseThemeTokens.spacing.md),
    content: @Composable BoxScope.() -> Unit
) {
    PulseSurface(
        modifier = modifier,
        tone = PulseSurfaceTone.Elevated,
        shape = PulseThemeTokens.radii.large,
        borderColor = if (selected) PulseColors.BorderFocused else PulseColors.BorderSubtle,
        tonalElevation = if (selected) 10.dp else 4.dp,
        contentPadding = contentPadding,
        content = content
    )
}
