package ar.com.scacchi.nightmare.ui

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
        .graphicsLayer(
            scaleX = uiState.transformation.zoom,
            scaleY = uiState.transformation.zoom,
            rotationZ = uiState.transformation.rotation,
            translationX = uiState.transformation.pan.x,
            translationY = uiState.transformation.pan.y
        )
    ) {
        val wadData = uiState.engine.wadData

        wadData.lineDefs.lineDefs.forEach { line ->
            drawLine(
                color = Color.Red,
                start = wadData.vertexes[line.startVertexId.toInt()].toOffset(),
                end = wadData.vertexes[line.endVertexId.toInt()].toOffset(),
            )
        }


        val points = uiState.engine.wadData.vertexes.vertexes
            .map { Offset(it.x.toFloat(), it.y.toFloat()) }

        drawPoints(
            points = points,
            pointMode = PointMode.Points,
            color = Color.Red,
            strokeWidth = 10f,
            cap = StrokeCap.Round
        )
    }
}
