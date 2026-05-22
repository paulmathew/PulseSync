package com.paulmathew.pulsesync.ui.diagnostics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.paulmathew.pulsesync.ui.theme.PulseColors
import com.paulmathew.pulsesync.ui.theme.PulseThemeTokens

@Composable
fun DiagnosticsScreenScaffold(
    title: String,
    description: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PulseColors.BackgroundPrimary),
    ) {
        DiagnosticsTopBar(
            title = title,
            description = description,
            onBackClick = onBackClick
        )
        content()
    }
}

@Composable
fun DiagnosticsTopBar(
    title: String,
    description: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    androidx.compose.foundation.layout.Row(
        modifier = modifier.fillMaxWidth().padding(top = PulseThemeTokens.spacing.xs),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = "Back",
                tint = PulseColors.TextPrimary
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = PulseColors.TextPrimary,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = description,
                color = PulseColors.TextSecondary,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}