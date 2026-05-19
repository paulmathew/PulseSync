package com.paulmathew.pulsesync.ui.conflict

data class ConflictUiState(
    val conflicts: List<ConflictItemUiState>,
    val selectedConflict: ConflictDetailUiState?
)

data class ConflictItemUiState(
    val operationId: String,
    val resourcePath: String,
    val detectedAtLabel: String,
    val localSummary: String,
    val remoteSummary: String
)

data class ConflictDetailUiState(
    val operationId: String,
    val resourcePath: String,
    val detectedAtLabel: String,
    val localVersion: ConflictVersionUiState,
    val remoteVersion: ConflictVersionUiState,
    val selectedStrategy: ConflictResolutionStrategyUi
)

data class ConflictVersionUiState(
    val versionId: String,
    val updatedAtLabel: String,
    val payloadHash: String,
    val summary: String
)

enum class ConflictResolutionStrategyUi {
    LocalWins,
    RemoteWins,
    Merge,
    ManualReview
}