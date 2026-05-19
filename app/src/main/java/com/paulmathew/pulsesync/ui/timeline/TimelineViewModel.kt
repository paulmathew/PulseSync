package com.paulmathew.pulsesync.ui.timeline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paulmathew.pulsesync.sync.runtime.SyncOrchestrator
import com.paulmathew.pulsesync.sync.runtime.SyncRuntimeProvider
import com.paulmathew.pulsesync.ui.mapping.toTimelineUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class TimelineViewModel @Inject constructor(
    private val orchestrator: SyncOrchestrator
) : ViewModel() {

    private val selectedFilter = MutableStateFlow(TimelineFilter.All)

    val uiState: StateFlow<TimelineUiState> = combine(
        orchestrator.state,
        selectedFilter
    ) { runtimeState, filter ->
        runtimeState.toTimelineUiState(selectedFilter = filter)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = orchestrator.state.value.toTimelineUiState()
    )

    fun onFilterSelected(filter: TimelineFilter) {
        selectedFilter.value = filter
    }
}