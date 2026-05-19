package com.paulmathew.pulsesync.sync.conflict

data class SyncConflict(
    val operationId: String,
    val resourcePath: String,
    val detectedAtMillis: Long,
    val localVersion: ConflictingVersion,
    val remoteVersion: ConflictingVersion
)

data class ConflictingVersion(
    val versionId: String,
    val updatedAtMillis: Long,
    val payloadHash: String,
    val summary: String
)