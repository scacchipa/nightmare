package ar.com.scacchi.nightmare.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import ar.com.scacchi.nightmare.ui.pad.Pad
import ar.com.scacchi.nightmare.ui.render.mainview.User3dViewRender
import ar.com.scacchi.nightmare.ui.render.map.MapViewRender

@Composable
fun MainScreen(
    vm: MainViewModel = hiltViewModel()
) {
    val uiState by vm.uiState.collectAsState()

    Column {

        User3dViewRender(
            engine = uiState.engine
        )
        Pad(
            playerActionSetChanged = {
                println(it)
                vm.onNewKeySet(it)
            }
        )

        MapViewRender(
            modifier = Modifier.fillMaxSize(),
            engine = uiState.engine
        )
    }
}
