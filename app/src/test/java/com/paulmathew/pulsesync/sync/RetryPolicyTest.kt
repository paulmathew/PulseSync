package com.paulmathew.pulsesync.sync

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class RetryPolicyTest {

    @Test
    fun nextRetryDelayMillis_usesExponentialBackoff() {
        val policy = RetryPolicy(
            maxAttempts = 4,
            baseDelayMillis = 1_000,
            maxDelayMillis = 30_000
        )

        assertEquals(1_000L, policy.nextRetryDelayMillis(0))
        assertEquals(2_000L, policy.nextRetryDelayMillis(1))
        assertEquals(4_000L, policy.nextRetryDelayMillis(2))
    }

    @Test
    fun nextRetryDelayMillis_returnsNullWhenAttemptsExhausted() {
        val policy = RetryPolicy(
            maxAttempts = 3,
            baseDelayMillis = 1_000,
            maxDelayMillis = 30_000
        )

        assertNull(policy.nextRetryDelayMillis(3))
    }

    @Test
    fun nextRetryDelayMillis_capsAtMaxDelay() {
        val policy = RetryPolicy(
            maxAttempts = 10,
            baseDelayMillis = 10_000,
            maxDelayMillis = 30_000
        )

        assertEquals(30_000L, policy.nextRetryDelayMillis(3))
    }
}