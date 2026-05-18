package com.paulmathew.pulsesync.ui.observability

data class ObservabilityUiState(
    val totalEvents: Int,
    val successCount: Int,
    val failureCount: Int,
    val retryScheduledCount: Int,
    val successRateLabel: String,
    val failureReasons: List<FailureReasonMetric>
)

data class FailureReasonMetric(
    val label: String,
    val count: Int
)