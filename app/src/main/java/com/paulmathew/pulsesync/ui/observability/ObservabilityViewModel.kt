package com.paulmathew.pulsesync.ui.observability

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paulmathew.pulsesync.sync.runtime.SyncOrchestrator
import com.paulmathew.pulsesync.sync.runtime.SyncRuntimeProvider
import com.paulmathew.pulsesync.ui.mapping.toObservabilityUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ObservabilityViewModel(
    orchestrator: SyncOrchestrator = SyncRuntimeProvider.orchestrator
) : ViewModel() {

    val uiState: StateFlow<ObservabilityUiState> = orchestrator.state
        .map { runtimeState ->
            runtimeState.toObservabilityUiState()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = orchestrator.state.value.toObservabilityUiState()
        )
}