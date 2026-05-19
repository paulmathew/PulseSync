package com.paulmathew.pulsesync.sync.runtime

import com.paulmathew.pulsesync.model.NetworkProfile
import com.paulmathew.pulsesync.model.NetworkProfileType
import com.paulmathew.pulsesync.sync.SyncAttemptResult
import com.paulmathew.pulsesync.sync.SyncFailureReason

object NetworkSyncOutcomePolicy {

    fun resultFor(profile: NetworkProfile): SyncAttemptResult {
        return when {
            profile.isOffline -> SyncAttemptResult.Failure(SyncFailureReason.Offline)
            profile.timeoutMs != null -> SyncAttemptResult.Failure(SyncFailureReason.Timeout)
            profile.type == NetworkProfileType.Poor -> {
                SyncAttemptResult.Failure(SyncFailureReason.ServerUnavailable)
            }
            profile.type == NetworkProfileType.PacketLoss -> {
                SyncAttemptResult.Failure(SyncFailureReason.Timeout)
            }
            else -> SyncAttemptResult.Success
        }
    }
}