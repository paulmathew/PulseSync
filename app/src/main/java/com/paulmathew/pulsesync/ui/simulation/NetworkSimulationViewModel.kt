package com.paulmathew.pulsesync.ui.simulation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paulmathew.pulsesync.model.NetworkProfile
import com.paulmathew.pulsesync.sync.runtime.SyncOrchestrator
import com.paulmathew.pulsesync.ui.mapping.toNetworkSimulationUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class NetworkSimulationViewModel @Inject constructor(
    private val orchestrator: SyncOrchestrator
) : ViewModel() {

    val uiState: StateFlow<NetworkSimulationUiState> = orchestrator.state
        .map { runtimeState ->
            runtimeState.toNetworkSimulationUiState()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = orchestrator.state.value.toNetworkSimulationUiState()
        )

    fun onProfileSelected(profile: NetworkProfile) {
        orchestrator.selectNetworkProfile(profile)
    }

    fun onSimulationToggleRequested() {
        val currentlyRunning = uiState.value.isSimulationRunning
        orchestrator.setNetworkSimulationRunning(!currentlyRunning)
    }
}