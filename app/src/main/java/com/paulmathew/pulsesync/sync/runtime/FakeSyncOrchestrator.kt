package com.paulmathew.pulsesync.sync.runtime

import com.paulmathew.pulsesync.sync.RetryPolicy
import com.paulmathew.pulsesync.sync.SyncAttemptResult
import com.paulmathew.pulsesync.sync.SyncEngineEvent
import com.paulmathew.pulsesync.sync.SyncFailureReason
import com.paulmathew.pulsesync.sync.SyncOperation
import com.paulmathew.pulsesync.sync.SyncOperationStatus
import com.paulmathew.pulsesync.sync.SyncStateReducer
import com.paulmathew.pulsesync.sync.conflict.ConflictResolutionResult
import com.paulmathew.pulsesync.sync.conflict.ConflictResolutionStrategy
import com.paulmathew.pulsesync.sync.conflict.ConflictResolver
import com.paulmathew.pulsesync.sync.conflict.ConflictingVersion
import com.paulmathew.pulsesync.sync.conflict.SyncConflict
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
            val createdConflict = when (result) {
                is SyncAttemptResult.Failure -> {
                    if (result.reason == SyncFailureReason.Conflict) {
                        activeOperation.toConflict(detectedAtMillis = nowMillis)
                    } else {
                        null
                    }
                }
                SyncAttemptResult.Success -> null
            }

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
                activeOperationId = null,
                conflicts = if (createdConflict != null) {
                    current.conflicts
                        .filterNot { it.operationId == createdConflict.operationId } + createdConflict
                } else {
                    current.conflicts
                }
            )
        }
    }

    override fun resolveConflict(
        operationId: String,
        strategy: ConflictResolutionStrategy,
        resolvedAtMillis: Long
    ): ConflictResolutionResult? {
        var result: ConflictResolutionResult? = null

        _state.update { current ->
            val conflict = current.conflicts.firstOrNull {
                it.operationId == operationId
            } ?: return@update current

            val resolutionResult = ConflictResolver.resolve(
                conflict = conflict,
                strategy = strategy,
                resolvedAtMillis = resolvedAtMillis
            )

            result = resolutionResult

            when (resolutionResult) {
                is ConflictResolutionResult.Resolved -> current.copy(
                    conflicts = current.conflicts.filterNot {
                        it.operationId == operationId
                    }
                )

                is ConflictResolutionResult.RequiresManualReview -> current
            }
        }

        return result
    }
    private fun List<SyncOperation>.replaceOperation(
        operation: SyncOperation
    ): List<SyncOperation> {
        return map { existing ->
            if (existing.id == operation.id) operation else existing
        }
    }
    private fun SyncOperation.toConflict(
        detectedAtMillis: Long
    ): SyncConflict {
        return SyncConflict(
            operationId = id,
            resourcePath = resourcePath,
            detectedAtMillis = detectedAtMillis,
            localVersion = ConflictingVersion(
                versionId = "local-$id",
                updatedAtMillis = createdAtMillis,
                payloadHash = payloadHash,
                summary = "Local pending mutation for $resourcePath"
            ),
            remoteVersion = ConflictingVersion(
                versionId = "remote-$id",
                updatedAtMillis = detectedAtMillis,
                payloadHash = "remote-$payloadHash",
                summary = "Remote version already changed for $resourcePath"
            )
        )
    }
}