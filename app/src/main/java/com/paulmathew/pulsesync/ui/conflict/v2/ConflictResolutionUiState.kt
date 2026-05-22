package com.paulmathew.pulsesync.ui.conflict.v2

import com.paulmathew.pulsesync.model.conflict.CollaborationConflict
import com.paulmathew.pulsesync.model.conflict.ConflictResolutionChoice

data class ConflictResolutionUiState(
    val conflict: CollaborationConflict,
    val selectedChoice: ConflictResolutionChoice = ConflictResolutionChoice.KeepMine
)