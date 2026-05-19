package com.paulmathew.pulsesync.ui.conflict

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paulmathew.pulsesync.sync.runtime.SyncOrchestrator
import com.paulmathew.pulsesync.ui.mapping.toConflictUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class ConflictViewModel @Inject constructor(
    private val orchestrator: SyncOrchestrator
) : ViewModel() {

    private val selectedConflictId = MutableStateFlow<String?>(null)
    private val selectedStrategy = MutableStateFlow(ConflictResolutionStrategyUi.LocalWins)

    val uiState: StateFlow<ConflictUiState> = combine(
        orchestrator.state,
        selectedConflictId,
        selectedStrategy
    ) { runtimeState, conflictId, strategy ->
        runtimeState.toConflictUiState(
            selectedConflictId = conflictId,
            selectedStrategy = strategy
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = orchestrator.state.value.toConflictUiState()
    )

    fun onConflictSelected(operationId: String) {
        selectedConflictId.value = operationId
    }

    fun onStrategySelected(strategy: ConflictResolutionStrategyUi) {
        selectedStrategy.value = strategy
    }

    fun onSelectionCleared() {
        selectedConflictId.value = null
    }
}