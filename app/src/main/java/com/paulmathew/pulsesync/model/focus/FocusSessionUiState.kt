package com.paulmathew.pulsesync.model.focus

import com.paulmathew.pulsesync.model.focus.FocusSession

data class FocusSessionUiState(
    val session: FocusSession,
    val draftText: String = ""
)