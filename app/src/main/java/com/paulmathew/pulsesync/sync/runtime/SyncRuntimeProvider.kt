package com.paulmathew.pulsesync.sync.runtime

object SyncRuntimeProvider {
    val orchestrator: SyncOrchestrator by lazy {
        FakeSyncOrchestrator().also { orchestrator ->
            SyncRuntimeSeed.seed(orchestrator)
        }
    }
}