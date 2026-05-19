package com.paulmathew.pulsesync.sync.runtime

import com.paulmathew.pulsesync.sync.SyncEngineEvent
import com.paulmathew.pulsesync.sync.SyncOperation
import com.paulmathew.pulsesync.sync.conflict.SyncConflict

data class SyncRuntimeState(
    val operations: List<SyncOperation>,
    val events: List<SyncEngineEvent>,
    val activeOperationId: String?,
    val conflicts: List<SyncConflict>,
    val networkSimulation: NetworkSimulationRuntimeState,

) {
    companion object {
        val Empty = SyncRuntimeState(
            operations = emptyList(),
            events = emptyList(),
            activeOperationId = null,
            conflicts = emptyList(),
            networkSimulation = NetworkSimulationRuntimeState.Default,
        )
    }
}