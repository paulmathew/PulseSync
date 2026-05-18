package com.paulmathew.pulsesync.ui.mapping

internal fun Long.toRuntimeTimestampLabel(): String {
    val totalSeconds = this / 1_000
    val minutes = (totalSeconds / 60) % 60
    val seconds = totalSeconds % 60

    return "%02d:%02d".format(minutes, seconds)
}