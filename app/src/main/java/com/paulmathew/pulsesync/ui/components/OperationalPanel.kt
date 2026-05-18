package com.paulmathew.pulsesync.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.paulmathew.pulsesync.ui.theme.FailureRed
import com.paulmathew.pulsesync.ui.theme.InformationalBlue
import com.paulmathew.pulsesync.ui.theme.OperationalGreen
import com.paulmathew.pulsesync.ui.theme.PanelBorder
import com.paulmathew.pulsesync.ui.theme.PanelSurface
import com.paulmathew.pulsesync.ui.theme.TextPrimary
import com.paulmathew.pulsesync.ui.theme.TextSecondary
import com.paulmathew.pulsesync.ui.theme.WarningAmber

@Composable
fun OperationalPanel(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(14.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .background(
                color = PanelSurface,
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                border = BorderStroke(1.dp, PanelBorder),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(contentPadding),
        content = content
    )
}

@Composable
fun StatusBadge(
    label: String,
    tone: StatusTone,
    modifier: Modifier = Modifier
) {
    val toneColor = tone.color()

    Text(
        text = label,
        color = toneColor,
        modifier = modifier
            .background(
                color = toneColor.copy(alpha = 0.12f),
                shape = RoundedCornerShape(6.dp)
            )
            .border(
                border = BorderStroke(1.dp, toneColor.copy(alpha = 0.45f)),
                shape = RoundedCornerShape(6.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
    )
}

@Composable
fun MetricTile(
    label: String,
    value: String,
    tone: StatusTone,
    modifier: Modifier = Modifier
) {
    OperationalPanel(
        modifier = modifier,
        contentPadding = PaddingValues(10.dp)
    ) {
        Text(
            text = label,
            color = TextSecondary
        )

        Text(
            text = value,
            color = tone.color()
        )
    }
}

enum class StatusTone {
    Success,
    Warning,
    Error,
    Info,
    Neutral
}

@Composable
fun StatusTone.color(): Color {
    return when (this) {
        StatusTone.Success -> OperationalGreen
        StatusTone.Warning -> WarningAmber
        StatusTone.Error -> FailureRed
        StatusTone.Info -> InformationalBlue
        StatusTone.Neutral -> TextPrimary
    }
}