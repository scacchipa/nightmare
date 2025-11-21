package ar.com.scacchi.nightmare.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import ar.com.scacchi.nightmare.ui.render.MapRender

@Composable
fun MainScreen(
    vm: MainViewModel = hiltViewModel()
) {
    val uiState by vm.uiState.collectAsState()

    MapRender(
        modifier = Modifier
            .fillMaxSize(),
        engine = uiState.engine
    )
}
