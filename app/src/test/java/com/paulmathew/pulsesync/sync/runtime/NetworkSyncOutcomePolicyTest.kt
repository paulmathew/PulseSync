package com.paulmathew.pulsesync.sync.runtime

import com.paulmathew.pulsesync.model.NetworkProfileType
import com.paulmathew.pulsesync.sync.SyncAttemptResult
import com.paulmathew.pulsesync.sync.SyncFailureReason
import org.junit.Assert.assertTrue
import org.junit.Assert.assertEquals
import org.junit.Test

class NetworkSyncOutcomePolicyTest {

    @Test
    fun resultFor_goodNetwork_returnsSuccess() {
        val result = NetworkSyncOutcomePolicy.resultFor(NetworkProfiles.goodNetwork)

        assertEquals(SyncAttemptResult.Success, result)
    }

    @Test
    fun resultFor_timeoutNetwork_returnsTimeoutFailure() {
        val profile = NetworkProfiles.all.first {
            it.type == NetworkProfileType.Timeout
        }

        val result = NetworkSyncOutcomePolicy.resultFor(profile)

        assertTrue(result is SyncAttemptResult.Failure)
        result as SyncAttemptResult.Failure
        assertEquals(SyncFailureReason.Timeout, result.reason)
    }

    @Test
    fun resultFor_offlineNetwork_returnsOfflineFailure() {
        val profile = NetworkProfiles.all.first {
            it.type == NetworkProfileType.Offline
        }

        val result = NetworkSyncOutcomePolicy.resultFor(profile)

        assertTrue(result is SyncAttemptResult.Failure)
        result as SyncAttemptResult.Failure
        assertEquals(SyncFailureReason.Offline, result.reason)
    }
}