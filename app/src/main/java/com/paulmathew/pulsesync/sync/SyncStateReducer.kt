package com.paulmathew.pulsesync.sync

object SyncStateReducer {

    fun markInFlight(
        operation: SyncOperation,
        nowMillis: Long
    ): SyncTransition {
        val next = operation.copy(
            status = SyncOperationStatus.InFlight
        )

        return SyncTransition(
            previous = operation,
            next = next,
            event = SyncEngineEvent.OperationStarted(
                operationId = operation.id,
                occurredAtMillis = nowMillis
            )
        )
    }

    fun applyAttemptResult(
        operation: SyncOperation,
        result: SyncAttemptResult,
        retryPolicy: RetryPolicy,
        nowMillis: Long
    ): SyncTransition {
        return when (result) {
            SyncAttemptResult.Success -> markSynced(
                operation = operation,
                nowMillis = nowMillis
            )

            is SyncAttemptResult.Failure -> markFailed(
                operation = operation,
                reason = result.reason,
                retryPolicy = retryPolicy,
                nowMillis = nowMillis
            )
        }
    }

    private fun markSynced(
        operation: SyncOperation,
        nowMillis: Long
    ): SyncTransition {
        val next = operation.copy(
            status = SyncOperationStatus.Synced
        )

        return SyncTransition(
            previous = operation,
            next = next,
            event = SyncEngineEvent.OperationSynced(
                operationId = operation.id,
                occurredAtMillis = nowMillis
            )
        )
    }

    private fun markFailed(
        operation: SyncOperation,
        reason: SyncFailureReason,
        retryPolicy: RetryPolicy,
        nowMillis: Long
    ): SyncTransition {
        val nextAttemptCount = operation.attemptCount + 1
        val retryDelayMillis = retryPolicy.nextRetryDelayMillis(nextAttemptCount)
        val nextRetryAtMillis = retryDelayMillis?.let { nowMillis + it }

        val next = operation.copy(
            attemptCount = nextAttemptCount,
            status = SyncOperationStatus.Failed(
                reason = reason,
                nextRetryAtMillis = nextRetryAtMillis
            )
        )

        return SyncTransition(
            previous = operation,
            next = next,
            event = SyncEngineEvent.OperationFailed(
                operationId = operation.id,
                occurredAtMillis = nowMillis,
                reason = reason,
                nextRetryAtMillis = nextRetryAtMillis
            )
        )
    }
}