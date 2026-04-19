package ar.com.scacchi.nightmare

import androidx.compose.ui.geometry.Offset
import ar.com.scacchi.nightmare.engine.Node
import ar.com.scacchi.nightmare.engine.Player
import ar.com.scacchi.nightmare.engine.Seg
import ar.com.scacchi.nightmare.ext.AxisIntersection
import ar.com.scacchi.nightmare.ext.checkXAxisIntersection
import ar.com.scacchi.nightmare.ext.compareTo
import ar.com.scacchi.nightmare.ext.deRotateBY
import ar.com.scacchi.nightmare.ext.tan
import ar.com.scacchi.nightmare.ext.vectorToPoint
import ar.com.scacchi.nightmare.settings.H_WIDTH
import ar.com.scacchi.nightmare.settings.SCREEN_DIST
import ar.com.scacchi.nightmare.settings.leftLimitFOVVersor
import ar.com.scacchi.nightmare.settings.rightLimitFOVVersor
import kotlin.math.tan

data class  VertexOnScreen(
    val startX: Float,
    val endX: Float,
    val startVertexToPlayer: Offset
)

class BSP {

    companion object {
        const val SUB_SECTOR_IDENTIFIER = 0x8000 // 2**15 = 32768

        fun isOnBackSide(player: Player, node: Node): Boolean {
            val delta = player.pos - node.partition

            return delta.x * node.dPartition.y - delta.y * node.dPartition.x <= 0
        }

        fun angleToX(angle: Float): Float = H_WIDTH - SCREEN_DIST * tan(angle)
        fun vectorToX(vector: Offset): Float = H_WIDTH - SCREEN_DIST * vector.tan()

        fun addSegmentToFov(player: Player, seg: Seg): VertexOnScreen? {

            val startVectorToPlaver = player.pos.vectorToPoint(seg.startVertex.pos)
            val endVectorToPlayer = player.pos.vectorToPoint(seg.endVertex.pos)

            val cross = startVectorToPlaver.x * endVectorToPlayer.y - startVectorToPlaver.y * endVectorToPlayer.x
            if (cross >= 0) return null

            val viewStart = startVectorToPlaver.deRotateBY(player.dirVersor)
            val viewEnd = endVectorToPlayer.deRotateBY(player.dirVersor)

            if (viewStart > leftLimitFOVVersor && viewEnd > leftLimitFOVVersor) return null
            if (viewStart < rightLimitFOVVersor && viewEnd < rightLimitFOVVersor) return null
            if (Offset.checkXAxisIntersection(viewStart, viewEnd) == AxisIntersection.NEGATIVE)
                return null

            val finalStart =
                if (viewStart > leftLimitFOVVersor) leftLimitFOVVersor
                else viewStart
            val finalEnd =
                if (viewEnd < rightLimitFOVVersor) rightLimitFOVVersor
                else viewEnd

            val startX = vectorToX(finalStart)
            val endX = vectorToX(finalEnd)

            return VertexOnScreen(startX, endX, startVectorToPlaver)
        }

        inline fun isOutsideLeft(v: Offset): Boolean {
            val cross = leftLimitFOVVersor.x * v.y - leftLimitFOVVersor.y * v.x
            return cross >= 0
        }

        inline fun isOutsideRight(v: Offset): Boolean {
            val cross = rightLimitFOVVersor.x * v.y - rightLimitFOVVersor .y * v.x
            return cross < 0
        }
    }
}