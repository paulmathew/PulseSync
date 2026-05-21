package com.paulmathew.pulsesync.ui.theme

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.runtime.Immutable

@Immutable
data class PulseMotion(
    val quickMillis: Int = 180,
    val standardMillis: Int = 260,
    val emphasizedMillis: Int = 340
)

val PulseMotionDefaults = PulseMotion()
val PulseEaseOut = CubicBezierEasing(0.16f, 1.0f, 0.3f, 1.0f)
val PulseEaseInOut = CubicBezierEasing(0.65f, 0.0f, 0.35f, 1.0f)
