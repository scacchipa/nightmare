package ar.com.scacchi.nightmare.ui.render.map

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import ar.com.scacchi.nightmare.engine.GameState
import ar.com.scacchi.nightmare.ext.degreeToRadian
import ar.com.scacchi.nightmare.ext.rotateBy
import ar.com.scacchi.nightmare.settings.SCALE
import ar.com.scacchi.nightmare.source.wad.NmColor
import kotlin.random.Random

@Composable
fun ColumnScope.MapViewRender(
    modifier: Modifier,
    gameState: GameState
) {
    var scale by remember { mutableFloatStateOf(1 / SCALE) }
    var offset by remember { mutableStateOf(Offset(0f, 2200f)) }
    var rotation by remember { mutableFloatStateOf(0f) }

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .clipToBounds()
            .pointerInput(Unit) {
                detectTransformGestures { centroid, pan, zoom, gestureRotation ->
                    println("centroid: $centroid, pan: $pan, zoom: $zoom, rotation: $gestureRotation")

                    val oldScale = scale

                    val newScale = oldScale * zoom
                    offset =
                        (offset + centroid / oldScale).rotateBy(gestureRotation.degreeToRadian()) -
                                (centroid / newScale + pan / oldScale)
                    scale = newScale
                    rotation += gestureRotation
                }
            }
            .graphicsLayer(
                scaleX = scale,
                scaleY = -scale,
                translationX = -offset.x * scale,
                translationY = -offset.y * scale,
                rotationZ = rotation,
                transformOrigin = TransformOrigin(0f, 0f),
            )
    ) {

        val mapViewDrawScope = MapViewDrawScope(this)
        with(mapViewDrawScope) {
            drawCircle(
                color = Color.Black,
                radius = 20f,
                center = Offset.Zero
            )

            drawLineDefs(
                lineDefs = gameState.episodeMap.lineDefs,
            )

            drawVertexes(
                vertexes = gameState.episodeMap.vertexes
            )

            drawPlayer(
                player = gameState.player
            )

            drawFov(gameState)
        }
    }
}


fun getColor(seed: Int): NmColor {
    val random = Random(seed)
    return NmColor(
        alpha = 0xFFu.toUByte(),
        red = random.nextInt(256).toUByte(),
        green = random.nextInt(256).toUByte(),
        blue = random.nextInt(256).toUByte(),
    )
}