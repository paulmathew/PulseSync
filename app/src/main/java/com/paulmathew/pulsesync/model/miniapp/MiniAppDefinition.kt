package com.paulmathew.pulsesync.model.miniapp

data class MiniAppDefinition(
    val id: String,
    val title: String,
    val subtitle: String,
    val statusLabel: String,
    val type: MiniAppType
)

enum class MiniAppType {
    FocusSession,
    MeetingNotes,
    SharedBrainstorm,
    OfflineJournal
}