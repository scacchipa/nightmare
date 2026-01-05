package ar.com.scacchi.nightmare.ui.render.mainview

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import ar.com.scacchi.nightmare.BSP
import ar.com.scacchi.nightmare.engine.Engine
import ar.com.scacchi.nightmare.settings.SCREEN_HEIGHT
import ar.com.scacchi.nightmare.ui.render.map.getColor

class User3dViewDrawScope(
    private val parentScope: DrawScope
) : DrawScope by parentScope {

    fun renderBspNode(engine: Engine, nodeId: Int) {
        if (nodeId >= BSP.Companion.SUB_SECTOR_IDENTIFIER) {
            this.renderSubSector(
                engine = engine,
                subSectorId = nodeId - BSP.Companion.SUB_SECTOR_IDENTIFIER
            )
            return
        }

        val node = engine.nodes[nodeId]

        if (BSP.Companion.isOnBackSide(engine.player, node)) {
            renderBspNode(engine, node.backChildId.toInt())
            if (BSP.Companion.checkBBox(engine.player, node.frontBoundBox)) {
                renderBspNode(engine, node.frondChildId.toInt())
            }
        } else {
            renderBspNode(engine, node.frondChildId.toInt())
            if (BSP.Companion.checkBBox(engine.player, node.backBoundBox)) {
                renderBspNode(engine, node.backChildId.toInt())
            }
        }
    }

    fun renderSubSector(engine: Engine, subSectorId: Int) {
        println("renderSubSector: $subSectorId")
        val subSector = engine.subSectors[subSectorId]

        for (segId in 0 until subSector.segCount) {
            val seg = engine.segs[subSector.firstSegId + segId]

            val result = BSP.Companion.addSegmentToFov(
                engine.player, seg.startVertex, seg.endVertex
            ) ?: continue

            println("result: $result")

            drawVLines(engine, result.startX, result.endX, subSectorId)
        }
    }


    fun drawVLines(engine: Engine, x1: Float, x2: Float, subSectorId: Int) {
        val color = getColor(subSectorId)

        this.drawLine(
            color = color,
            start = Offset(x1, 0f),
            end = Offset(x1, SCREEN_HEIGHT),
            strokeWidth = 1f,
        )
        this.drawLine(
            color = color,
            start = Offset(x2, 0f),
            end = Offset(x2, SCREEN_HEIGHT),
            strokeWidth = 1f,
        )
    }
}