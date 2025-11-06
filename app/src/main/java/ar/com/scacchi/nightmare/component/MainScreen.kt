package ar.com.scacchi.nightmare.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun MainScreen(
    vm: MainViewModel = hiltViewModel()
) {
    val uiState by vm.uiState.collectAsState()

    Canvas(modifier = Modifier
        .fillMaxSize()
        .graphicsLayer(
            scaleX = uiState.transformation.zoom,
            scaleY = uiState.transformation.zoom,
            rotationZ = uiState.transformation.rotation,
            translationX = uiState.transformation.pan.x,
            translationY = uiState.transformation.pan.y
        )
        .pointerInput(Unit) {
            detectTransformGestures { centroid, pan, zoom, rotation ->
                println("centroid: $centroid, pan: $pan, zoom: $zoom, rotation: $rotation")
                vm.updateTransformation(
                        pan = pan,
                        zoom = zoom,
                        rotation = rotation
                )
            }
        }
    ) {
        val width = size.width
        val height = size.height

        val points = uiState.engine.wadData.vertexes
            .map { Offset(it.x.toFloat()/4, it.y.toFloat()/4 + height) }
        points.forEach { println("${it.x}:${it.y}") }


        drawPoints(
            points = points,
            pointMode = PointMode.Points,
            color = Color.Red,
            strokeWidth = 2f,
            cap = StrokeCap.Round
        )
    }
}