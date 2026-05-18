package com.paulmathew.pulsesync.sync

sealed interface SyncFailureReason {
    data object Timeout : SyncFailureReason
    data object Offline : SyncFailureReason
    data object ServerUnavailable : SyncFailureReason
    data object Conflict : SyncFailureReason
    data class Unknown(val message: String) : SyncFailureReason
}