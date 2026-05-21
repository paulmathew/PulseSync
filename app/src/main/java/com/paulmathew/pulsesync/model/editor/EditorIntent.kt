package com.paulmathew.pulsesync.model.editor

sealed interface EditorIntent {
    data class TitleChanged(val value: String) : EditorIntent
    data class BodyChanged(val value: String) : EditorIntent
    data object SaveAcknowledged : EditorIntent
    data object SyncStarted : EditorIntent
    data object WentOffline : EditorIntent
    data object RetryStarted : EditorIntent
}