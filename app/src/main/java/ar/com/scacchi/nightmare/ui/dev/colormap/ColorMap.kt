package ar.com.scacchi.nightmare.ui.dev.colormap

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun ColorMap(
    viewModel: ColorMapViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val colorMap = state.engine.colorMap
    val playPal = state.engine.playPal[0]
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier.horizontalScroll(state = scrollState)
    ) {
        for (idx in 0 until colorMap.tableCount) {
            Row{
                val colorTable = colorMap[idx]
                for (idy in 0 until 0x100) {
                    Box(
                        modifier = Modifier
                            .size(4.dp)
                            .background(playPal[colorTable[idy].toInt()])
                    )
                }
            }
        }
    }
}

