package com.paulmathew.pulsesync.ui.mapping

import com.paulmathew.pulsesync.model.OperationStatus
import com.paulmathew.pulsesync.model.SyncHealthStatus
import com.paulmathew.pulsesync.model.SyncMetric
import com.paulmathew.pulsesync.sync.SyncOperationStatus
import com.paulmathew.pulsesync.sync.runtime.SyncRuntimeState
import com.paulmathew.pulsesync.ui.dashboard.DashboardUiState

fun SyncRuntimeState.toDashboardUiState(): DashboardUiState {
    val pendingCount = operations.count { it.status is SyncOperationStatus.Pending }
    val syncingCount = operations.count { it.status is SyncOperationStatus.InFlight }
    val failedCount = operations.count { it.status is SyncOperationStatus.Failed }
    val syncedCount = operations.count { it.status is SyncOperationStatus.Synced }

    val totalCount = operations.size.coerceAtLeast(1)
    val progressPercent = ((syncedCount.toFloat() / totalCount) * 100).toInt()

    val healthStatus = when {
        operations.any { it.status is SyncOperationStatus.Failed } -> SyncHealthStatus.Degraded
        operations.any { it.status is SyncOperationStatus.InFlight } -> SyncHealthStatus.Healthy
        operations.isEmpty() -> SyncHealthStatus.Offline
        else -> SyncHealthStatus.Healthy
    }

    return DashboardUiState(
        healthStatus = healthStatus,
        healthLabel = healthStatus.label,
        healthDescription = healthStatus.description,
        syncProgressPercent = progressPercent,
        metrics = listOf(
            SyncMetric("Pending", pendingCount.toString(), OperationStatus.Pending),
            SyncMetric("Syncing", syncingCount.toString(), OperationStatus.Syncing),
            SyncMetric("Failed", failedCount.toString(), OperationStatus.Failed),
            SyncMetric("Synced", syncedCount.toString(), OperationStatus.Synced)
        ),
        recentEvents = events.takeLast(4)
            .map { it.toSyncTimelineEvent() }
            .asReversed(),
        lastSyncLabel = "Not available",
        nextAttemptLabel = "Not scheduled"
    )
}

private val SyncHealthStatus.label: String
    get() = when (this) {
        SyncHealthStatus.Healthy -> "Healthy"
        SyncHealthStatus.Degraded -> "Degraded"
        SyncHealthStatus.Failing -> "Failing"
        SyncHealthStatus.Offline -> "Offline"
    }

private val SyncHealthStatus.description: String
    get() = when (this) {
        SyncHealthStatus.Healthy -> "Sync pipeline operating normally"
        SyncHealthStatus.Degraded -> "Retries or failures require attention"
        SyncHealthStatus.Failing -> "Sync pipeline blocked by repeated failures"
        SyncHealthStatus.Offline -> "No runtime operations available"
    }