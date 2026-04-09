package ar.com.scacchi.nightmare.ui.render.map

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import ar.com.scacchi.nightmare.engine.GameState
import ar.com.scacchi.nightmare.engine.LineDefs
import ar.com.scacchi.nightmare.engine.Node
import ar.com.scacchi.nightmare.engine.Player
import ar.com.scacchi.nightmare.engine.Seg
import ar.com.scacchi.nightmare.engine.Vertexes
import ar.com.scacchi.nightmare.ext.atan2
import ar.com.scacchi.nightmare.ext.fromAngle
import ar.com.scacchi.nightmare.settings.H_FOV
import ar.com.scacchi.nightmare.settings.SCREEN_HEIGHT

class MapViewDrawScope(
    private val parentScope: DrawScope
) : DrawScope by parentScope {

    fun drawLineDefs(
        lineDefs: LineDefs,
    ) {
        lineDefs.content.forEach { line ->
            drawLine(
                color = Color.Red,
                start = line.startVertex.pos,
                end = line.endVertex.pos,
            )
        }
    }

    fun drawVertexes(
        vertexes: Vertexes
    ) {
        drawPoints(
            points = vertexes.getOffsets(),
            pointMode = PointMode.Points,
            color = Color.Red,
            strokeWidth = 10f,
            cap = StrokeCap.Round
        )
    }

    fun drawPlayer(player: Player) {
        drawCircle(
            color = Color.Black,
            radius = 20f,
            center = player.pos
        )
    }

    fun drawFov(gameState: GameState) {
        val playerPos = gameState.player.pos

        val angle = gameState.player.dirVector.atan2()
        val dirA1 = Offset.fromAngle(angle - H_FOV)
        val dirA2 = Offset.fromAngle(angle + H_FOV)

        val lenRay = SCREEN_HEIGHT

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

    fun drawNode(gameState: GameState, nodeId: Int) {
        val node = gameState.episodeMap.nodes[nodeId]

        drawBBox(
            bBox = node.frontBoundBox,
            color = Color.Green
        )
        drawBBox(
            bBox = node.backBoundBox,
            color = Color.Red
        )

        drawLine(
            color = Color.Blue,
            start = node.partition,
            end = node.partition + node.dPartition,
            strokeWidth = 10f
        )
    }

    fun drawBBox(bBox: Node.BoundBox, color: Color) {
        val w = bBox.right - bBox.left
        val h = bBox.bottom - bBox.top
        drawRect(
            color = color,
            topLeft = bBox.leftTop,
            size = Size(w, h),
            style = Stroke(width = 10f)
        )
    }

    fun drawSeg(seg: Seg, subSectorId: Int) {
        val v1 = seg.startVertex
        val v2 = seg.endVertex
        drawLine(
            color = getColor(subSectorId).color,
            start = v1.pos,
            end = v2.pos,
            strokeWidth = 10f
        )
    }
}