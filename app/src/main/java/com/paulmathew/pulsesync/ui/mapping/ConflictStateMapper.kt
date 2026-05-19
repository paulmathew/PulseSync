package com.paulmathew.pulsesync.ui.mapping

import com.paulmathew.pulsesync.sync.conflict.ConflictingVersion
import com.paulmathew.pulsesync.sync.conflict.SyncConflict
import com.paulmathew.pulsesync.sync.runtime.SyncRuntimeState
import com.paulmathew.pulsesync.ui.conflict.ConflictDetailUiState
import com.paulmathew.pulsesync.ui.conflict.ConflictItemUiState
import com.paulmathew.pulsesync.ui.conflict.ConflictResolutionStrategyUi
import com.paulmathew.pulsesync.ui.conflict.ConflictUiState
import com.paulmathew.pulsesync.ui.conflict.ConflictVersionUiState

fun SyncRuntimeState.toConflictUiState(
    selectedConflictId: String? = null,
    selectedStrategy: ConflictResolutionStrategyUi = ConflictResolutionStrategyUi.LocalWins
): ConflictUiState {
    val selectedConflict = conflicts.firstOrNull {
        it.operationId == selectedConflictId
    }

    return ConflictUiState(
        conflicts = conflicts.map { it.toConflictItemUiState() },
        selectedConflict = selectedConflict?.toConflictDetailUiState(
            selectedStrategy = selectedStrategy
        )
    )
}

private fun SyncConflict.toConflictItemUiState(): ConflictItemUiState {
    return ConflictItemUiState(
        operationId = operationId,
        resourcePath = resourcePath,
        detectedAtLabel = detectedAtMillis.toRuntimeTimestampLabel(),
        localSummary = localVersion.summary,
        remoteSummary = remoteVersion.summary
    )
}

private fun SyncConflict.toConflictDetailUiState(
    selectedStrategy: ConflictResolutionStrategyUi
): ConflictDetailUiState {
    return ConflictDetailUiState(
        operationId = operationId,
        resourcePath = resourcePath,
        detectedAtLabel = detectedAtMillis.toRuntimeTimestampLabel(),
        localVersion = localVersion.toConflictVersionUiState(),
        remoteVersion = remoteVersion.toConflictVersionUiState(),
        selectedStrategy = selectedStrategy
    )
}

private fun ConflictingVersion.toConflictVersionUiState(): ConflictVersionUiState {
    return ConflictVersionUiState(
        versionId = versionId,
        updatedAtLabel = updatedAtMillis.toRuntimeTimestampLabel(),
        payloadHash = payloadHash,
        summary = summary
    )
}