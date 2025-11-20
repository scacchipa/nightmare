package ar.com.scacchi.nightmare.ui

import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import ar.com.scacchi.nightmare.ui.render.MapRender

@Composable
fun MainScreen(
    vm: MainViewModel = hiltViewModel()
) {
    val uiState by vm.uiState.collectAsState()

    MapRender(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTransformGestures { centroid, pan, zoom, rotation ->
                    println("centroid: $centroid, pan: $pan, zoom: $zoom, rotation: $rotation")

                    vm.updateTransformation(
                        pan = pan,
                        zoom = zoom,
                        rotation = rotation
                    )
                }
            },
        transformation = uiState.transformation,
        engine = uiState.engine
    )
}
