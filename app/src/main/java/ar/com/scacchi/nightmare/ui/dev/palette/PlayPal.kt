package ar.com.scacchi.nightmare.ui.dev.palette

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import ar.com.scacchi.nightmare.ui.widget.NumSpinner

@Composable
fun PlayPal(
    viewModel: PlayPalViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    NumSpinner(
        value = state.currentPaletteSelected + 1,
        maxValue = state.playPal.paletteCount,
        onValueChange = { viewModel.setPaletteSelected(it - 1) }
    )

    Column {
        for (idx in 0 until 0xFF step 0x10) {
            Row {
                for (idy in 0 until 0x10) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(state.playPal[state.currentPaletteSelected][idx + idy])
                            .border(1.dp, Color.Black)
                    )
                }
            }
        }
    }
}