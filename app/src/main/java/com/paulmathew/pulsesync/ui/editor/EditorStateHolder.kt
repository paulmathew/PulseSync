package com.paulmathew.pulsesync.ui.editor

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.paulmathew.pulsesync.model.editor.EditorIntent
import com.paulmathew.pulsesync.model.editor.EditorStateReducer
import com.paulmathew.pulsesync.model.editor.EditorUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class EditorStateHolder(
    initialState: EditorUiState,
    private val scope: CoroutineScope
) {
    var state by mutableStateOf(initialState)
        private set

    private var autosaveJob: Job? = null

    fun dispatch(intent: EditorIntent) {
        state = EditorStateReducer.reduce(state, intent)

        when (intent) {
            is EditorIntent.TitleChanged,
            is EditorIntent.BodyChanged -> scheduleAutosave()

            else -> Unit
        }
    }

    private fun scheduleAutosave() {
        autosaveJob?.cancel()
        autosaveJob = scope.launch {
            delay(700)
            dispatch(EditorIntent.SaveAcknowledged)
        }
    }
}