package com.paulmathew.pulsesync.ui.dashboard

import androidx.lifecycle.ViewModel
import com.paulmathew.pulsesync.sync.runtime.SyncOrchestrator
import com.paulmathew.pulsesync.sync.runtime.SyncRuntimeProvider
import com.paulmathew.pulsesync.ui.mapping.toDashboardUiState
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import androidx.lifecycle.viewModelScope
import com.paulmathew.pulsesync.sync.SyncAttemptResult
import com.paulmathew.pulsesync.sync.SyncFailureReason
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val orchestrator: SyncOrchestrator
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

    fun onStartNextSync(){
        orchestrator.startNext(nowMillis = System.currentTimeMillis())
    }
    fun onCompleteActiveAsSuccess(){
        orchestrator.completeActive(
            result = SyncAttemptResult.Success,
            nowMillis = System.currentTimeMillis()
        )
    }
    fun onCompleteActiveAsTimeout() {
        orchestrator.completeActive(
            result = SyncAttemptResult.Failure(SyncFailureReason.Timeout),
            nowMillis = System.currentTimeMillis()
        )
    }
    fun onCompleteActiveFromNetworkProfile() {
        val profile = orchestrator.state.value.networkSimulation.selectedProfile

        val result = when {
            profile.isOffline -> SyncAttemptResult.Failure(SyncFailureReason.Offline)
            profile.timeoutMs != null -> SyncAttemptResult.Failure(SyncFailureReason.Timeout)
            profile.packetLossPercent >= 20 -> SyncAttemptResult.Failure(SyncFailureReason.ServerUnavailable)
            else -> SyncAttemptResult.Success
        }

        orchestrator.completeActive(
            result = result,
            nowMillis = System.currentTimeMillis()
        )
    }
    fun onCompleteActiveUsingNetworkProfile() {
        orchestrator.completeActiveUsingNetworkProfile(
            nowMillis = System.currentTimeMillis()
        )
    }
}