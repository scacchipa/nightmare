package ar.com.scacchi.nightmare

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import ar.com.scacchi.nightmare.engine.Engine
import ar.com.scacchi.nightmare.engine.Node
import ar.com.scacchi.nightmare.engine.Nodes
import ar.com.scacchi.nightmare.engine.Player
import ar.com.scacchi.nightmare.engine.Segs
import ar.com.scacchi.nightmare.engine.SubSectors
import ar.com.scacchi.nightmare.settings.FOV
import ar.com.scacchi.nightmare.settings.H_FOV
import ar.com.scacchi.nightmare.ui.render.drawSeg
import kotlin.math.atan2

class BSP(val engine: Engine) {
    val player: Player = engine.player
    val nodes: Nodes = engine.nodes
    val subSectors: SubSectors = engine.subSectors
    val segs: Segs = engine.segs
    val rootNodeId: Int = this.nodes.count() - 1

    val SUB_SECTOR_IDENTIFIER = 0x8000 // 2**15 = 32768

    fun update(drawScope: DrawScope) {
        renderBspNode(drawScope, rootNodeId)
    }

    fun renderSubSector(subSectorId: Int, drawScope: DrawScope) {
        println("renderSubSector: $subSectorId")
        val subSector = subSectors[subSectorId]

        for (segId in 0 until subSector.segCount) {
            val seg = segs[subSector.firstSegId + segId]
            drawScope.drawSeg(engine, seg, subSectorId)
        }
    }

    fun renderBspNode(drawScope: DrawScope, nodeId: Int) {
        if (nodeId >= SUB_SECTOR_IDENTIFIER) {
            renderSubSector(
                subSectorId = nodeId - SUB_SECTOR_IDENTIFIER,
                drawScope = drawScope)
            return
        }

        val node = nodes[nodeId]

        if (isOnBackSide(node)) {
            renderBspNode(drawScope, node.backChildId.toInt())
            if (checkBBox(node.frontBoundBox)) {
                renderBspNode(drawScope, node.frondChildId.toInt())
            }
        } else {
            renderBspNode(drawScope, node.frondChildId.toInt())
            if (checkBBox(node.backBoundBox)) {
                renderBspNode(drawScope, node.backChildId.toInt())
            }
        }
    }

    fun isOnBackSide(node: Node): Boolean {
        val dx = player.xPos - node.xPartition
        val dy = player.yPos - node.yPartition

        return dx * node.dxPartition - dy * node.dyPartition <= 0
    }

    fun checkBBox(bBox: Node.BoundBox): Boolean {

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
            val angle1 = this.pointToAngle(v1)
            val angle2 = this.pointToAngle(v2)

            val span = norm(angle1 - angle2)

            val adjustedAngle1 = angle1 - this.player.angle
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

    fun pointToAngle(vertex: Offset): Float {
        val deltaX = vertex.x - engine.player.xPos
        val deltaY = vertex.y - engine.player.yPos

        return atan2(deltaY, deltaX)
    }

    companion object {
        fun norm(angle: Float): Float {
            val angle = (angle % (2 * Math.PI)).toFloat()
            return if (angle >= 0) angle else (angle + 2 * Math.PI).toFloat()
        }
    }
}