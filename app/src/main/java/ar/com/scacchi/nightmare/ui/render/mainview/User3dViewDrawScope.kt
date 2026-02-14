package ar.com.scacchi.nightmare.ui.render.mainview

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import ar.com.scacchi.nightmare.BSP
import ar.com.scacchi.nightmare.SegHandler
import ar.com.scacchi.nightmare.engine.Engine
import ar.com.scacchi.nightmare.engine.GameState
import ar.com.scacchi.nightmare.settings.SCREEN_HEIGHT
import ar.com.scacchi.nightmare.ui.render.map.getColor
import kotlin.random.Random

class User3dViewDrawScope(
    private val parentScope: DrawScope,
    gameState: GameState,
) : DrawScope by parentScope {

    var isTraverseBsp = true
    var segHandler: SegHandler = SegHandler(this, gameState.player)

    fun renderBspNode(gameState: GameState, nodeId: Int) {
        if (isTraverseBsp.not()) {

            if (nodeId >= BSP.SUB_SECTOR_IDENTIFIER) {
                this.renderSubSector(
                    gameState = gameState,
                    subSectorId = nodeId - BSP.SUB_SECTOR_IDENTIFIER
                )
                return
            }

            val node = gameState.nodes[nodeId]

            if (BSP.isOnBackSide(gameState.player, node)) {
                renderBspNode(gameState, node.backChildId.toInt())
                if (BSP.checkBBox(gameState.player, node.frontBoundBox)) {
                    renderBspNode(gameState, node.frondChildId.toInt())
                }
            } else {
                renderBspNode(gameState, node.frondChildId.toInt())
                if (BSP.checkBBox(gameState.player, node.backBoundBox)) {
                    renderBspNode(gameState, node.backChildId.toInt())
                }
            }
        }
    }

    fun renderSubSector(gameState: GameState, subSectorId: Int) {

        val subSector = gameState.subSectors[subSectorId]

        for (segId in 0 until subSector.segCount) {
            val seg = gameState.segs[subSector.firstSegId + segId]

            val result = BSP.addSegmentToFov(
                gameState.player, seg.startVertex, seg.endVertex
            ) ?: continue

//            drawVLines(engine, result.startX, result.endX, subSectorId)
            segHandler.classifySegment(seg, result.startX.toInt(), result.endX.toInt(), result.realWallAngle)
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

    val colorMap = mutableMapOf<String, Color>()
    fun drawVLine(x: Int, y1: Int, y2: Int, tex: String, light: Int) {
        if (y1 < y2) {
            this.drawLine(
                color = getColor(tex, light),
                start = Offset(x.toFloat(), y1.toFloat()),
                end = Offset(x.toFloat(), y2.toFloat()),
                strokeWidth = 2f

            )
        }
    }

    fun getColor(tex: String, lightLevel: Int): Color {
        val colorLabel = tex + lightLevel.toString()

        return colorMap.getOrPut(colorLabel) {
            val intensity = lightLevel / 255f
            val rnd = Random(tex.hashCode())
            Color(
                red = rnd.nextInt(50, 256) / 256f * intensity,
                green = rnd.nextInt(50, 256) / 256f * intensity,
                blue = rnd.nextInt(50, 256) / 256f * intensity,
            )
        }
    }
}