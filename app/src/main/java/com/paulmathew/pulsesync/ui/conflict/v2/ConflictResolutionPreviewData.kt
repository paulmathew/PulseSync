package com.paulmathew.pulsesync.ui.conflict.v2

import com.paulmathew.pulsesync.model.conflict.CollaborationConflict
import com.paulmathew.pulsesync.model.conflict.ConflictVersion

object ConflictResolutionPreviewData {

    val default = ConflictResolutionUiState(
        conflict = CollaborationConflict(
            id = "conflict-project-aurora",
            documentTitle = "Project Aurora",
            subtitle = "Edited on 2 devices",
            yourVersion = ConflictVersion(
                label = "Your Version",
                authorLabel = "You",
                updatedAtLabel = "Today, 9:14 AM",
                content = "Offline-first is a feature that empowers users to stay productive anytime, anywhere."
            ),
            remoteVersion = ConflictVersion(
                label = "Remote Version",
                authorLabel = "Sarah",
                updatedAtLabel = "Today, 9:16 AM",
                content = "Offline-first is a feature that ensures reliability and builds trust with our users."
            ),
            mergePreview = "Offline-first is a feature that empowers users to stay productive anywhere while ensuring reliability and trust."
        )
    )
}