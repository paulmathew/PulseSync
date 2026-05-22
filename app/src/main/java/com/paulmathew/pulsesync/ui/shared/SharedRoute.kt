package com.paulmathew.pulsesync.ui.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.paulmathew.pulsesync.ui.components.NoSharedItemsEmptyState
import com.paulmathew.pulsesync.ui.theme.PulseColors
import com.paulmathew.pulsesync.ui.theme.PulseThemeTokens

@Composable
fun SharedRoute(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(PulseColors.BackgroundPrimary)
            .padding(PulseThemeTokens.spacing.lg),
        contentAlignment = Alignment.Center
    ) {
        NoSharedItemsEmptyState()
    }
}