package com.paulmathew.pulsesync.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.dp

@Immutable
data class PulseRadii(
    val small: RoundedCornerShape = RoundedCornerShape(8.dp),
    val medium: RoundedCornerShape = RoundedCornerShape(16.dp),
    val large: RoundedCornerShape = RoundedCornerShape(24.dp),
    val extraLarge: RoundedCornerShape = RoundedCornerShape(32.dp),
    val full: RoundedCornerShape = RoundedCornerShape(percent = 50)
)

val PulseRadius = PulseRadii()

val PulseShapes = Shapes(
    extraSmall = PulseRadius.small,
    small = PulseRadius.small,
    medium = PulseRadius.medium,
    large = PulseRadius.large,
    extraLarge = PulseRadius.extraLarge
)
