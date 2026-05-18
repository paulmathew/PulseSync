package com.paulmathew.pulsesync.sync

sealed interface SyncEngineEvent {
    val operationId: String
    val occurredAtMillis: Long

    data class OperationStarted(
        override val operationId: String,
        override val occurredAtMillis: Long
    ) : SyncEngineEvent

    data class OperationSynced(
        override val operationId: String,
        override val occurredAtMillis: Long
    ) : SyncEngineEvent

    data class OperationFailed(
        override val operationId: String,
        override val occurredAtMillis: Long,
        val reason: SyncFailureReason,
        val nextRetryAtMillis: Long?
    ) : SyncEngineEvent

    data class RetryScheduled(
        override val operationId: String,
        override val occurredAtMillis: Long,
        val nextRetryAtMillis: Long
    ) : SyncEngineEvent
}