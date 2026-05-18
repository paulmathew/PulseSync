package com.paulmathew.pulsesync.ui.queue

import com.paulmathew.pulsesync.model.QueuedOperation

data class QueueUiState(
    val selectedFilter: QueueFilter,
    val filters: List<QueueFilter>,
    val operations: List<QueuedOperation>
)

enum class QueueFilter {
    All,
    Pending,
    Syncing,
    Failed
}
val QueueFilter.label: String
    get() = when (this) {
        QueueFilter.All -> "All"
        QueueFilter.Pending -> "Pending"
        QueueFilter.Syncing -> "Syncing"
        QueueFilter.Failed -> "Failed"
    }