package com.paulmathew.pulsesync.sync.runtime

import com.paulmathew.pulsesync.sync.RetryPolicy
import com.paulmathew.pulsesync.sync.SyncAttemptResult
import com.paulmathew.pulsesync.sync.SyncEngineEvent
import com.paulmathew.pulsesync.sync.SyncOperation
import com.paulmathew.pulsesync.sync.SyncOperationStatus
import com.paulmathew.pulsesync.sync.SyncStateReducer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class FakeSyncOrchestrator(
    private val retryPolicy: RetryPolicy = RetryPolicy.Default
) : SyncOrchestrator {

    private val _state = MutableStateFlow(SyncRuntimeState.Empty)

    override val state: StateFlow<SyncRuntimeState> = _state

    override fun enqueue(operation: SyncOperation) {
        _state.update { current ->
            current.copy(
                operations = current.operations + operation
            )
        }
    }

    override fun startNext(nowMillis: Long) {
        _state.update { current ->
            if (current.activeOperationId != null) return@update current

            val nextOperation = current.operations.firstOrNull { operation ->
                val status = operation.status
                status is SyncOperationStatus.Pending ||
                        (status is SyncOperationStatus.Failed &&
                                status.nextRetryAtMillis != null &&
                                nowMillis >= status.nextRetryAtMillis)
            } ?: return@update current

            val transition = SyncStateReducer.markInFlight(
                operation = nextOperation,
                nowMillis = nowMillis
            )

            current.copy(
                operations = current.operations.replaceOperation(transition.next),
                events = current.events + transition.event,
                activeOperationId = transition.next.id
            )
        }
    }

    override fun completeActive(
        result: SyncAttemptResult,
        nowMillis: Long
    ) {
        _state.update { current ->
            val activeOperationId = current.activeOperationId ?: return@update current
            val activeOperation = current.operations.firstOrNull { it.id == activeOperationId }
                ?: return@update current

            val transition = SyncStateReducer.applyAttemptResult(
                operation = activeOperation,
                result = result,
                retryPolicy = retryPolicy,
                nowMillis = nowMillis
            )

            val events = buildList {
                addAll(current.events)
                add(transition.event)

                val failedStatus = transition.next.status as? SyncOperationStatus.Failed
                val nextRetryAtMillis = failedStatus?.nextRetryAtMillis
                if (nextRetryAtMillis != null) {
                    add(
                        SyncEngineEvent.RetryScheduled(
                            operationId = transition.next.id,
                            occurredAtMillis = nowMillis,
                            nextRetryAtMillis = nextRetryAtMillis
                        )
                    )
                }
            }

            current.copy(
                operations = current.operations.replaceOperation(transition.next),
                events = events,
                activeOperationId = null
            )
        }
    }

    private fun List<SyncOperation>.replaceOperation(
        operation: SyncOperation
    ): List<SyncOperation> {
        return map { existing ->
            if (existing.id == operation.id) operation else existing
        }
    }
}