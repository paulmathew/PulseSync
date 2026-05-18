package com.paulmathew.pulsesync.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.paulmathew.pulsesync.model.SyncTimelineEvent
import com.paulmathew.pulsesync.model.TimelineEventSeverity
import com.paulmathew.pulsesync.ui.theme.TextPrimary
import com.paulmathew.pulsesync.ui.theme.TextSecondary

@Composable
fun TimelineEventRow(
    event: SyncTimelineEvent,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(vertical = 8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = event.timestamp,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.width(12.dp))

        Box(
            modifier = Modifier
                .padding(top = 4.dp)
                .size(8.dp)
                .background(
                    color = event.severity.toStatusTone().color(),
                    shape = CircleShape
                )
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = event.title,
                color = TextPrimary
            )

            Text(
                text = event.detail,
                color = TextSecondary
            )
        }
    }
}

fun TimelineEventSeverity.toStatusTone(): StatusTone {
    return when (this) {
        TimelineEventSeverity.Success -> StatusTone.Success
        TimelineEventSeverity.Info -> StatusTone.Info
        TimelineEventSeverity.Warning -> StatusTone.Warning
        TimelineEventSeverity.Error -> StatusTone.Error
    }
}