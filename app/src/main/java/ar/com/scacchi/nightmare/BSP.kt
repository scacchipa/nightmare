package ar.com.scacchi.nightmare

import ar.com.scacchi.nightmare.engine.Node
import ar.com.scacchi.nightmare.engine.Player
import ar.com.scacchi.nightmare.engine.Vertex
import ar.com.scacchi.nightmare.ext.angleToX
import ar.com.scacchi.nightmare.ext.normalizeAngle
import ar.com.scacchi.nightmare.settings.FOV
import ar.com.scacchi.nightmare.settings.H_FOV
import ar.com.scacchi.nightmare.settings.H_WIDTH
import ar.com.scacchi.nightmare.settings.SCREEN_DIST
import kotlin.math.PI
import kotlin.math.tan

data class VertexOnScreen(
    val startX: Float,
    val endX: Float,
    val realWallAngle: Float
)

class BSP {

    companion object {
        const val SUB_SECTOR_IDENTIFIER = 0x8000 // 2**15 = 32768
        const val TWO_PI = (2f * Math.PI).toFloat()

        fun isOnBackSide(player: Player, node: Node): Boolean {
            val delta = player.pos - node.partition

            return delta.x * node.dPartition.y - delta.y * node.dPartition.x <= 0
        }

        fun angleToX(angle: Float): Float = H_WIDTH - SCREEN_DIST * tan(angle)

        fun addSegmentToFov(player: Player, startVertex: Vertex, endVertex: Vertex): VertexOnScreen? {
            val realStartAngle = player.pos.angleToX(startVertex.pos)
            val realEndAngle = player.pos.angleToX(endVertex.pos)

            val span = (realStartAngle - realEndAngle).normalizeAngle()

            // backface culling
            if (span >= PI) return null

            val startAngle = realStartAngle - player.angle
            val endAngle = realEndAngle - player.angle

            val startSpan = (H_FOV + startAngle).normalizeAngle()

            val startClippedAngle =
                if (startSpan > FOV) {
                    if (startSpan >= span + FOV) return null
                    H_FOV
                } else startAngle

            val endSpan = (H_FOV - endAngle).normalizeAngle()

            val endClippedAngle =
                if (endSpan > FOV) {
                    if (endSpan >= span + FOV) return null
                    -H_FOV
                } else endAngle

            val startX = angleToX(startClippedAngle)
            val endX = angleToX(endClippedAngle)

            return VertexOnScreen(startX, endX, realStartAngle)
        }
    }
}