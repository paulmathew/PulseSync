package com.paulmathew.pulsesync.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Alignment
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.paulmathew.pulsesync.ui.theme.PulseTheme
import com.paulmathew.pulsesync.ui.theme.PulseThemeTokens

@Composable
fun WorkspaceCardSkeleton(
    modifier: Modifier = Modifier
) {
    PulseSurface(
        modifier = modifier.fillMaxWidth(),
        tone = PulseSurfaceTone.Base,
        shape = PulseThemeTokens.radii.large,
        contentPadding = PaddingValues(PulseThemeTokens.spacing.md)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(PulseThemeTokens.spacing.md)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(PulseThemeTokens.spacing.xs)) {
                    PulseSkeletonText(width = 150.dp, height = 20.dp)
                    PulseSkeletonText(width = 110.dp, height = 12.dp)
                }

                PulseSkeleton(modifier = Modifier.size(10.dp))
            }

            Row(horizontalArrangement = Arrangement.spacedBy((-8).dp)) {
                PulseSkeleton(modifier = Modifier.size(28.dp))
                PulseSkeleton(modifier = Modifier.size(28.dp))
                PulseSkeleton(modifier = Modifier.size(28.dp))
            }

            PulseSkeletonText(width = 260.dp, height = 14.dp)
            PulseSkeletonText(width = 220.dp, height = 14.dp)
        }
    }
}

@Composable
fun ActivityEventSkeleton(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(PulseThemeTokens.spacing.md),
        verticalAlignment = Alignment.Top
    ) {
        PulseSkeleton(modifier = Modifier.size(34.dp))

        PulseSurface(
            modifier = Modifier.weight(1f),
            tone = PulseSurfaceTone.Base,
            shape = PulseThemeTokens.radii.large,
            contentPadding = PaddingValues(PulseThemeTokens.spacing.md)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(PulseThemeTokens.spacing.sm)) {
                PulseSkeletonText(width = 190.dp, height = 18.dp)
                PulseSkeletonText(width = 240.dp, height = 13.dp)
                PulseSkeletonText(width = 70.dp, height = 12.dp)
            }
        }
    }
}

@Composable
fun EditorLoadingSkeleton(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(PulseThemeTokens.spacing.lg)
    ) {
        PulseSkeletonText(width = 250.dp, height = 42.dp)
        PulseSkeletonText(width = 320.dp, height = 16.dp)
        PulseSkeletonText(width = 280.dp, height = 16.dp)
        PulseSkeletonText(width = 300.dp, height = 16.dp)
    }
}

@Composable
fun QueueDrawerSkeleton(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(PulseThemeTokens.spacing.md)
    ) {
        PulseSkeletonText(width = 150.dp, height = 24.dp)
        repeat(3) {
            WorkspaceCardSkeleton()
        }
    }
}
@Preview
@Composable
private fun WorkspaceCardSkeletonPreview() {
    PulseTheme {
        WorkspaceCardSkeleton()
    }
}