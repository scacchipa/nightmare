package ar.com.scacchi.nightmare

import androidx.compose.ui.graphics.drawscope.DrawScope
import ar.com.scacchi.nightmare.engine.Engine
import ar.com.scacchi.nightmare.engine.Node
import ar.com.scacchi.nightmare.engine.Nodes
import ar.com.scacchi.nightmare.engine.Player
import ar.com.scacchi.nightmare.engine.Segs
import ar.com.scacchi.nightmare.engine.SubSectors
import ar.com.scacchi.nightmare.ui.render.drawSeg

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
            renderBspNode(drawScope, node.frondChildId.toInt())
        } else {
            renderBspNode(drawScope, node.frondChildId.toInt())
            renderBspNode(drawScope, node.backChildId.toInt())
        }
    }

    fun isOnBackSide(node: Node): Boolean {
        val dx = player.xPos - node.xPartition
        val dy = player.yPos - node.yPartition

        return dx * node.dxPartition - dy * node.dyPartition <= 0

    }
}