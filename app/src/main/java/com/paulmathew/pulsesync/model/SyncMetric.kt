package com.paulmathew.pulsesync.model

data class SyncMetric(
    val label: String,
    val value: String,
    val status: OperationStatus
)