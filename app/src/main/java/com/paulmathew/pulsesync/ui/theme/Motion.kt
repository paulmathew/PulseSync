package com.paulmathew.pulsesync.ui.theme

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Immutable

val PulseEaseOut = CubicBezierEasing(0.16f, 1.0f, 0.3f, 1.0f)
val PulseEaseInOut = CubicBezierEasing(0.65f, 0.0f, 0.35f, 1.0f)

@Immutable
data class PulseMotion(
    val fastDurationMillis: Int = 160,
    val standardDurationMillis: Int = 220,
    val sheetDurationMillis: Int = 260,
    val pressScale: Float = 0.985f,
    val fastFade: FiniteAnimationSpec<Float> = tween(
        durationMillis = fastDurationMillis,
        easing = PulseEaseOut
    ),
    val standardFade: FiniteAnimationSpec<Float> = tween(
        durationMillis = standardDurationMillis,
        easing = PulseEaseOut
    ),
    val sheetTransition: FiniteAnimationSpec<Float> = tween(
        durationMillis = sheetDurationMillis,
        easing = PulseEaseInOut
    ),
    val gentleSpring: SpringSpec<Float> = spring(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessMediumLow
    ),
    val pressSpring: SpringSpec<Float> = spring(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessMedium
    )
)

val PulseMotionDefaults = PulseMotion()
