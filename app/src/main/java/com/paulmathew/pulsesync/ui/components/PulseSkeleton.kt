package com.paulmathew.pulsesync.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.paulmathew.pulsesync.ui.theme.PulseColors
import com.paulmathew.pulsesync.ui.theme.PulseEaseOut
import com.paulmathew.pulsesync.ui.theme.PulseThemeTokens

@Composable
fun PulseSkeleton(
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "PulseSkeleton")
    val shimmerOffset by transition.animateFloat(
        initialValue = -300f,
        targetValue = 600f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1600,
                easing = PulseEaseOut
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "PulseSkeletonShimmer"
    )

    val brush = Brush.linearGradient(
        colors = listOf(
            PulseColors.SurfaceSecondary.copy(alpha = 0.38f),
            PulseColors.SurfaceSecondary.copy(alpha = 0.62f),
            PulseColors.SurfaceSecondary.copy(alpha = 0.38f)
        ),
        start = Offset(shimmerOffset, 0f),
        end = Offset(shimmerOffset + 260f, 0f)
    )

    Box(
        modifier = modifier
            .alpha(0.9f)
            .background(
                brush = brush,
                shape = PulseThemeTokens.radii.medium
            )
    )
}

@Composable
fun PulseSkeletonText(
    width: Dp,
    height: Dp = 14.dp,
    modifier: Modifier = Modifier
) {
    PulseSkeleton(
        modifier = modifier
            .width(width)
            .height(height)
    )
}