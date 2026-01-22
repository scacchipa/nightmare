package ar.com.scacchi.nightmare

import androidx.compose.ui.geometry.Offset
import ar.com.scacchi.nightmare.engine.Node
import ar.com.scacchi.nightmare.engine.Player
import ar.com.scacchi.nightmare.engine.Vertex
import ar.com.scacchi.nightmare.settings.FOV
import ar.com.scacchi.nightmare.settings.H_FOV
import ar.com.scacchi.nightmare.settings.H_WIDTH
import ar.com.scacchi.nightmare.settings.SCREEN_DIST
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.tan

data class VertexOnScreen(
    val startX: Float,
    val endX: Float,
    val realWallAngle: Float
)

class BSP {

    companion object {
        const val SUB_SECTOR_IDENTIFIER = 0x8000 // 2**15 = 32768

        fun norm(angle: Float): Float {
            val angle = (angle % (2 * Math.PI)).toFloat()
            return if (angle >= 0) angle else (angle + 2 * Math.PI).toFloat()
        }

        fun isOnBackSide(player: Player,node: Node): Boolean {
            val dx = player.xPos - node.xPartition
            val dy = player.yPos - node.yPartition

            return dx * node.dyPartition - dy * node.dxPartition <= 0
        }

        fun angleToX(angle: Float): Float {
            return H_WIDTH - SCREEN_DIST * tan(angle)
        }

        fun addSegmentToFov(player: Player, startVertex: Vertex, endVertex: Vertex): VertexOnScreen? {
            val realStartAngle = pointToAngle(player, startVertex.toOffset())
            val realEndAngle = pointToAngle(player, endVertex.toOffset())

            val span = norm(realStartAngle - realEndAngle)

            // backface culling
            if (span >= PI) return null

            val startAngle = realStartAngle - player.angle
            val endAngle = realEndAngle - player.angle

            val startSpan = norm(H_FOV + startAngle)

            val startClippedAngle =
                if (startSpan > FOV) {
                    if (startSpan >= span + FOV) return null
                    H_FOV
                } else startAngle

            val endSpan = norm(H_FOV - endAngle)

            val endClippedAngle =
                if (endSpan > FOV) {
                    if (endSpan >= span + FOV) return null
                    -H_FOV
                } else endAngle

            val startX = angleToX(startClippedAngle)
            val endX = angleToX(endClippedAngle)

            return VertexOnScreen(startX, endX, realStartAngle)
        }

        fun pointToAngle(player: Player, vertex: Offset): Float {
            val deltaX = vertex.x - player.xPos
            val deltaY = vertex.y - player.yPos

            return atan2(deltaY, deltaX)
        }

        fun checkBBox(player: Player, bBox: Node.BoundBox): Boolean {

            val a = Offset(bBox.left.toFloat(), bBox.bottom.toFloat())
            val b = Offset(bBox.left.toFloat(), bBox.top.toFloat())
            val c = Offset(bBox.right.toFloat(), bBox.top.toFloat())
            val d = Offset(bBox.right.toFloat(), bBox.bottom.toFloat())

            val px = player.xPos
            val py = player.yPos

            val bBoxSides: List<Pair<Offset, Offset>> = when {
                px < bBox.left -> when {
                    py > bBox.top -> listOf(Pair(b, a), Pair(c, b))
                    py < bBox.bottom -> listOf(Pair(b, a), Pair(a, d))
                    else -> listOf(Pair(b, a))
                }
                px > bBox.right -> when {
                    py > bBox.top -> listOf(Pair(c, b), Pair(d, c))
                    py < bBox.bottom -> listOf(Pair(a, d), Pair(d, c))
                    else -> listOf(Pair(d, c))
                }
                else -> when {
                    py > bBox.top -> listOf(Pair(c, b))
                    py < bBox.bottom -> listOf(Pair(a, d))
                    else -> return true
                }
            }

            for ((v1, v2) in bBoxSides) {
                val angle1 = pointToAngle(player, v1)
                val angle2 = pointToAngle(player, v2)

                val span = norm(angle1 - angle2)

                val adjustedAngle1 = angle1 - player.angle
                val span1 = norm(adjustedAngle1 + H_FOV)

                if (span1 > FOV) {
                    if (span1 >= span + FOV) {
                        continue
                    }
                }
                return true
            }
            return false
        }
    }
}