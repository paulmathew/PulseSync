package com.paulmathew.pulsesync.sync

data class RetryPolicy(
    val maxAttempts: Int,
    val baseDelayMillis: Long,
    val maxDelayMillis: Long
) {
    fun nextRetryDelayMillis(attemptCount: Int): Long? {
        if (attemptCount >= maxAttempts) return null

        val multiplier = 1L shl attemptCount.coerceAtMost(30)
        val delay = baseDelayMillis * multiplier

        return delay.coerceAtMost(maxDelayMillis)
    }
    companion object {
        val Default = RetryPolicy(
            maxAttempts = 3,
            baseDelayMillis = 1_000,
            maxDelayMillis = 30_000
        )
    }
}