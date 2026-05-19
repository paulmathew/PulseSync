package com.paulmathew.pulsesync.ui.mapping

import com.paulmathew.pulsesync.sync.conflict.ConflictingVersion
import com.paulmathew.pulsesync.sync.conflict.SyncConflict
import com.paulmathew.pulsesync.sync.runtime.SyncRuntimeState
import com.paulmathew.pulsesync.ui.conflict.ConflictResolutionStrategyUi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class ConflictStateMapperTest {

    private val localVersion = ConflictingVersion(
        versionId = "v1-local",
        updatedAtMillis = 1000L,
        payloadHash = "hash-local",
        summary = "Local change"
    )

    private val remoteVersion = ConflictingVersion(
        versionId = "v1-remote",
        updatedAtMillis = 1100L,
        payloadHash = "hash-remote",
        summary = "Remote change"
    )

    private val conflict1 = SyncConflict(
        operationId = "op-1",
        resourcePath = "/data/res1",
        detectedAtMillis = 1200L,
        localVersion = localVersion,
        remoteVersion = remoteVersion
    )

    private val conflict2 = SyncConflict(
        operationId = "op-2",
        resourcePath = "/data/res2",
        detectedAtMillis = 1300L,
        localVersion = localVersion.copy(summary = "Local 2"),
        remoteVersion = remoteVersion.copy(summary = "Remote 2")
    )

    private val runtimeState = SyncRuntimeState.Empty.copy(
        conflicts = listOf(conflict1, conflict2)
    )

    @Test
    fun `toConflictUiState maps all conflicts to item states`() {
        val uiState = runtimeState.toConflictUiState()

        assertEquals(2, uiState.conflicts.size)

        val item1 = uiState.conflicts[0]
        assertEquals(conflict1.operationId, item1.operationId)
        assertEquals(conflict1.resourcePath, item1.resourcePath)
        assertEquals("00:01", item1.detectedAtLabel)
        assertEquals(conflict1.localVersion.summary, item1.localSummary)
        assertEquals(conflict1.remoteVersion.summary, item1.remoteSummary)

        val item2 = uiState.conflicts[1]
        assertEquals(conflict2.operationId, item2.operationId)
        assertEquals(conflict2.resourcePath, item2.resourcePath)
        assertEquals("00:01", item2.detectedAtLabel)
        assertEquals(conflict2.localVersion.summary, item2.localSummary)
        assertEquals(conflict2.remoteVersion.summary, item2.remoteSummary)
    }

    @Test
    fun `toConflictUiState sets selectedConflict when valid id is provided`() {
        val strategy = ConflictResolutionStrategyUi.RemoteWins
        val uiState = runtimeState.toConflictUiState(
            selectedConflictId = "op-1",
            selectedStrategy = strategy
        )

        assertNotNull(uiState.selectedConflict)
        val detail = uiState.selectedConflict!!
        assertEquals("op-1", detail.operationId)
        assertEquals(strategy, detail.selectedStrategy)
        assertEquals(conflict1.localVersion.versionId, detail.localVersion.versionId)
        assertEquals(conflict1.remoteVersion.versionId, detail.remoteVersion.versionId)
    }

    @Test
    fun `toConflictUiState has null selectedConflict when id is null`() {
        val uiState = runtimeState.toConflictUiState(selectedConflictId = null)
        assertNull(uiState.selectedConflict)
    }

    @Test
    fun `toConflictUiState has null selectedConflict when id is not found`() {
        val uiState = runtimeState.toConflictUiState(selectedConflictId = "non-existent")
        assertNull(uiState.selectedConflict)
    }

    @Test
    fun `toConflictUiState uses default strategy when none provided`() {
        val uiState = runtimeState.toConflictUiState(selectedConflictId = "op-2")
        
        assertNotNull(uiState.selectedConflict)
        assertEquals(ConflictResolutionStrategyUi.LocalWins, uiState.selectedConflict?.selectedStrategy)
    }
}
