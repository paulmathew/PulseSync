package com.paulmathew.pulsesync.ui.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun OperationalPanel(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
){}
@Composable
fun StatusBadge(
    label: String,
    tone: StatusTone,
    modifier: Modifier = Modifier
){}
enum class StatusTone {
    Success,
    Warning,
    Error,
    Info,
    Neutral
}

@Composable
fun MetricTile(
    label: String,
    value: String,
    tone: StatusTone,
    modifier: Modifier = Modifier
){}