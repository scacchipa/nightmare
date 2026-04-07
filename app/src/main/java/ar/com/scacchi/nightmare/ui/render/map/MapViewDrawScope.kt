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
import ar.com.scacchi.nightmare.ext.scalar
import ar.com.scacchi.nightmare.settings.H_FOV
import ar.com.scacchi.nightmare.settings.SCREEN_HEIGHT

class MapViewDrawScope(
    private val parentScope: DrawScope
) : DrawScope by parentScope {

    fun drawLineDefs(
        lineDefs: LineDefs,
        vertexes: Vertexes,
    ) {
        lineDefs.content.forEach { line ->
            drawLine(
                color = Color.Red,
                start = vertexes[line.startVertexId].pos,
                end = vertexes[line.endVertexId].pos,
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

        val angle = gameState.player.angle
        val dirA1 = Offset.scalar(angle - H_FOV)
        val dirA2 = Offset.scalar(angle + H_FOV)

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
        val x = bBox.left
        val y = bBox.top
        val w = bBox.right - bBox.left
        val h = bBox.bottom - bBox.top
        drawRect(
            color = color,
            topLeft = Offset(x, y),
            size = Size(w, h),
            style = Stroke(width = 10f)
        )
    }

    fun drawSeg(gameState: GameState, seg: Seg, subSectorId: Int) {
        val v1 = gameState.episodeMap.vertexes[seg.startVertexId]
        val v2 = gameState.episodeMap.vertexes[seg.endVertexId]
        drawLine(
            color = getColor(subSectorId).color,
            start = v1.pos,
            end = v2.pos,
            strokeWidth = 10f
        )
    }
}