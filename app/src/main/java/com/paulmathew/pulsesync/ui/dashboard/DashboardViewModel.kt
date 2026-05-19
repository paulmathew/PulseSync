package com.paulmathew.pulsesync.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paulmathew.pulsesync.sync.SyncAttemptResult
import com.paulmathew.pulsesync.sync.SyncFailureReason
import com.paulmathew.pulsesync.sync.runtime.SyncOrchestrator
import com.paulmathew.pulsesync.ui.mapping.toDashboardUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
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
    fun onCompleteActiveUsingNetworkProfile() {
        orchestrator.completeActiveUsingNetworkProfile(
            nowMillis = System.currentTimeMillis()
        )
    }
    fun onCompleteActiveAsConflict() {
        orchestrator.completeActive(
            result = SyncAttemptResult.Failure(SyncFailureReason.Conflict),
            nowMillis = System.currentTimeMillis()
        )
    }
}