package com.paulmathew.pulsesync.sync

data class SyncTransition(
    val previous: SyncOperation,
    val next: SyncOperation,
    val event: SyncEngineEvent
)