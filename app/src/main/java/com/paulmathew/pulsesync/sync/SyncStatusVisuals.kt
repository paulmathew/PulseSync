package com.paulmathew.pulsesync.sync

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.paulmathew.pulsesync.model.sync.SyncStatus
import com.paulmathew.pulsesync.ui.theme.PulseColors

data class SyncStatusVisuals(
    val label: String,
    val description: String,
    val color: Color
)

@Composable
fun rememberSyncStatusVisuals(
    status: SyncStatus
): SyncStatusVisuals {
    return when (status) {
        SyncStatus.Synced -> SyncStatusVisuals(
            label = "Synced",
            description = "All changes are up to date.",
            color = PulseColors.TrustGreen
        )

        SyncStatus.Syncing -> SyncStatusVisuals(
            label = "Saving...",
            description = "Changes are being synced.",
            color = PulseColors.AccentPrimary
        )

        is SyncStatus.OfflinePending -> SyncStatusVisuals(
            label = "Saved offline",
            description = "${status.pendingChanges} changes will sync when online.",
            color = PulseColors.WarningAmber
        )

        is SyncStatus.Retrying -> SyncStatusVisuals(
            label = "Retrying",
            description = status.attemptLabel,
            color = PulseColors.WarningAmber
        )

        SyncStatus.NeedsAttention -> SyncStatusVisuals(
            label = "Needs review",
            description = "Some changes need your attention.",
            color = PulseColors.FailureRed
        )
    }
}