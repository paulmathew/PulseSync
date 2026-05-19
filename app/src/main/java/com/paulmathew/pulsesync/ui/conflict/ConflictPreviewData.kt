package com.paulmathew.pulsesync.ui.conflict

object ConflictPreviewData {

    val emptyState = ConflictUiState(
        conflicts = emptyList(),
        selectedConflict = null
    )

    val selectedConflictState = ConflictUiState(
        conflicts = listOf(
            ConflictItemUiState(
                operationId = "op-1041",
                resourcePath = "/notes/41",
                detectedAtLabel = "12:03",
                localSummary = "Design sync engine for offline-first apps",
                remoteSummary = "Design sync engine with server conflict handling"
            )
        ),
        selectedConflict = ConflictDetailUiState(
            operationId = "op-1041",
            resourcePath = "/notes/41",
            detectedAtLabel = "12:03",
            localVersion = ConflictVersionUiState(
                versionId = "local-op-1041",
                updatedAtLabel = "12:01",
                payloadHash = "hash-local-1041",
                summary = "Design sync engine for offline-first apps"
            ),
            remoteVersion = ConflictVersionUiState(
                versionId = "remote-op-1041",
                updatedAtLabel = "12:02",
                payloadHash = "hash-remote-1041",
                summary = "Design sync engine with server conflict handling"
            ),
            selectedStrategy = ConflictResolutionStrategyUi.LocalWins
        )
    )
}