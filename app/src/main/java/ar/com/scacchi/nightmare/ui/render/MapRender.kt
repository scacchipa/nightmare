package ar.com.scacchi.nightmare.ui.render

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import ar.com.scacchi.nightmare.engine.Engine
import ar.com.scacchi.nightmare.engine.LineDefs
import ar.com.scacchi.nightmare.engine.Node
import ar.com.scacchi.nightmare.engine.Player
import ar.com.scacchi.nightmare.engine.Seg
import ar.com.scacchi.nightmare.engine.Vertexes
import ar.com.scacchi.nightmare.ext.rotateBy
import ar.com.scacchi.nightmare.ext.scalar
import ar.com.scacchi.nightmare.settings.HEIGHT
import ar.com.scacchi.nightmare.settings.H_FOV
import ar.com.scacchi.nightmare.settings.SCALE
import kotlin.random.Random

@Composable
fun MapRender(
    modifier: Modifier,
    engine: Engine
) {
    var scale by remember { mutableFloatStateOf(1 / SCALE) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var rotation by remember { mutableFloatStateOf(0f) }

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTransformGestures { centroid, pan, zoom, gestureRotation ->
                    println("centroid: $centroid, pan: $pan, zoom: $zoom, rotation: $gestureRotation")
                    //println("scale: $scale, offset: $offset, rotation: $rotation")

                    val oldScale = scale

                    val newScale = oldScale * zoom
                    offset =
                        (offset + centroid / oldScale).rotateBy(gestureRotation) -
                                (centroid / newScale + pan / oldScale)
                    scale = newScale
                    rotation += gestureRotation
                }
            }
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale,
                translationX = - offset.x * scale,
                translationY = - offset.y * scale,
                rotationZ = rotation,
                transformOrigin = TransformOrigin(0f, 0f)
            )
    ) {
        drawCircle(
            color = Color.Black,
            radius = 20f,
            center = Offset.Zero)

        drawLineDefs(
            lineDefs = engine.lineDefs,
            vertexes = engine.vertexes
        )

        drawVertexes(
            vertexes = engine.vertexes
        )

        drawPlayer(
            player = engine.player
        )

//        drawNode(engine, engine.bsp.rootNodeId)

        engine.bsp.update(this)

        drawFov(engine)
    }
}


fun DrawScope.drawLineDefs(
    lineDefs: LineDefs,
    vertexes: Vertexes
) {
    lineDefs.content.forEach { line ->
        drawLine(
            color = Color.Companion.Red,
            start = vertexes[line.startVertexId.toInt()].toOffset(),
            end = vertexes[line.endVertexId.toInt()].toOffset(),
        )
    }
}

fun DrawScope.drawVertexes(
    vertexes: Vertexes
) {
    val points = vertexes.content
        .map { Offset(it.x.toFloat(), it.y.toFloat()) }

    drawPoints(
        points = points,
        pointMode = PointMode.Companion.Points,
        color = Color.Companion.Red,
        strokeWidth = 10f,
        cap = StrokeCap.Companion.Round
    )
}

fun DrawScope.drawPlayer(
    player: Player
) {
    println("drawPlayer: ${player.xPos}, ${player.yPos}")
    drawCircle(
        color = Color.Black,
        radius = 20f,
        center = Offset(player.xPos, player.yPos)
    )
}
fun DrawScope.drawFov(engine: Engine) {
    val playerPos = Offset(
        x = engine.player.xPos,
        y = engine.player.yPos,
    )

    println("RADIAN: ${engine.player.angle}")

    val angle = engine.player.angle
    val dirA1 = Offset.scalar(angle - H_FOV)
    val dirA2 = Offset.scalar(angle + H_FOV)

    val lenRay = HEIGHT

    this.drawLine(
        color = Color.Yellow,
        strokeWidth = 10f,
        start = playerPos,
        end = playerPos + dirA1 * lenRay * 5f
    )

    this.drawLine(
        color = Color.Yellow,
        strokeWidth = 10f,
        start = playerPos,
        end = playerPos + dirA2 * lenRay * 5f
    )
}

fun DrawScope.drawNode(engine: Engine, nodeId: Int) {
    val node = engine.nodes[nodeId]

    drawBBox(
        bBox = node.frontBoundBox,
        color = Color.Green)
    drawBBox(
        bBox = node.backBoundBox,
        color = Color.Red)

    drawLine(
        color = Color.Blue,
        start = Offset(
            x = node.xPartition.toFloat(),
            y = node.yPartition.toFloat()),
        end = Offset(
            x = (node.xPartition + node.dxPartition).toFloat(),
            y = (node.yPartition + node.dyPartition).toFloat()),
        strokeWidth = 10f
    )
}

fun DrawScope.drawBBox(bBox: Node.BoundBox, color: Color) {
    val x = bBox.left
    val y = bBox.top
    val w = bBox.right - bBox.left
    val h = bBox.bottom - bBox.top
    drawRect(
        color = color,
        topLeft = Offset(x.toFloat(), y.toFloat()),
        size = Size(w.toFloat(), h.toFloat()),
        style = Stroke(width = 10f)
    )
}

fun DrawScope.drawSeg(engine: Engine, seg: Seg, subSectorId: Int) {
    val v1 = engine.vertexes[seg.startVertexId.toInt()]
    val v2 = engine.vertexes[seg.endVertexId.toInt()]
    drawLine(
        color = getColor(subSectorId),
        start = v1.toOffset(),
        end = v2.toOffset(),
        strokeWidth = 10f
    )
}

fun getColor(seed: Int): Color {
    val random = Random(seed)
    return Color(
        red = random.nextFloat(),
        green = random.nextFloat(),
        blue = random.nextFloat())
}