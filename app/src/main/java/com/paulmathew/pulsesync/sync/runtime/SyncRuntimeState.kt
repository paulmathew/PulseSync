package com.paulmathew.pulsesync.sync.runtime

import com.paulmathew.pulsesync.sync.SyncEngineEvent
import com.paulmathew.pulsesync.sync.SyncOperation

data class SyncRuntimeState(
    val operations: List<SyncOperation>,
    val events: List<SyncEngineEvent>,
    val activeOperationId: String?
) {
    companion object {
        val Empty = SyncRuntimeState(
            operations = emptyList(),
            events = emptyList(),
            activeOperationId = null
        )
    }
}