package com.paulmathew.pulsesync.model.conflict

data class CollaborationConflict(
    val id: String,
    val documentTitle: String,
    val subtitle: String,
    val yourVersion: ConflictVersion,
    val remoteVersion: ConflictVersion,
    val mergePreview: String
)

data class ConflictVersion(
    val label: String,
    val authorLabel: String,
    val updatedAtLabel: String,
    val content: String
)

enum class ConflictResolutionChoice {
    KeepMine,
    UseTheirs,
    Merge
}