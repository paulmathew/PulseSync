package com.paulmathew.pulsesync.ui.queue

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paulmathew.pulsesync.model.QueuedOperation
import com.paulmathew.pulsesync.sync.runtime.SyncOrchestrator
import com.paulmathew.pulsesync.sync.runtime.SyncRuntimeProvider
import com.paulmathew.pulsesync.ui.mapping.toQueueUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class QueueViewModel @Inject constructor(
    private val orchestrator: SyncOrchestrator
) : ViewModel() {

    private val selectedFilter = MutableStateFlow(QueueFilter.All)

    val uiState: StateFlow<QueueUiState> = combine(
        orchestrator.state,
        selectedFilter
    ) { runtimeState, filter ->
        runtimeState.toQueueUiState(selectedFilter = filter)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = orchestrator.state.value.toQueueUiState()
    )

    fun onFilterSelected(filter: QueueFilter) {
        selectedFilter.value = filter
    }

    fun onOperationSelected(operation: QueuedOperation) {
        // Selection/details will be introduced with an operation inspector.
    }
}