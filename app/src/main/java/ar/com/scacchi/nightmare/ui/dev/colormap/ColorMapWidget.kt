package ar.com.scacchi.nightmare.ui.dev.colormap

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import ar.com.scacchi.nightmare.ui.widget.NumSpinner

@Composable
fun ColorMap(
    viewModel: ColorMapViewModel = hiltViewModel(),
) {
    val state by viewModel.uiStateFlow.collectAsState()
    val scrollState = rememberScrollState()

    Column {
        Row {
            Text(text = "Pallete Id:", fontSize = 32.sp)
            NumSpinner(
                value = state.paletteIdx + 1,
                maxValue = state.playPal.paletteCount,
                onValueChange = { viewModel.setPaletteSelected(it - 1) }
            )
        }
        Column(
            modifier = Modifier.horizontalScroll(state = scrollState)
        ) {
            for (idx in 0 until state.colorMap.tableCount) {
                Row {
                    for (idy in 0 until 0x100) {
                        val colorIdx = state.colorMap[idx][idy]
                        Box(
                            modifier = Modifier
                                .size(4.dp)
                                .background(state.playPal[state.paletteIdx][colorIdx.toInt()])
                        )
                    }
                }
            }
        }
    }
}