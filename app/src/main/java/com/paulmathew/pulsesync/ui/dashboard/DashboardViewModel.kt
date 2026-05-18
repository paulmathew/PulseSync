package com.paulmathew.pulsesync.ui.dashboard

import androidx.lifecycle.ViewModel
import com.paulmathew.pulsesync.sync.runtime.SyncOrchestrator
import com.paulmathew.pulsesync.sync.runtime.SyncRuntimeProvider
import com.paulmathew.pulsesync.ui.mapping.toDashboardUiState
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted

class DashboardViewModel(
    orchestrator: SyncOrchestrator = SyncRuntimeProvider.orchestrator
) : ViewModel() {

    val uiState: StateFlow<DashboardUiState> = orchestrator.state
        .map { runtimeState ->
            runtimeState.toDashboardUiState()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = orchestrator.state.value.toDashboardUiState()
        )
}