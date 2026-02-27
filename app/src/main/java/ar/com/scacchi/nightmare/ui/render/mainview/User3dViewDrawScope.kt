package ar.com.scacchi.nightmare.ui.render.mainview

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import ar.com.scacchi.nightmare.BSP
import ar.com.scacchi.nightmare.SegHandler
import ar.com.scacchi.nightmare.data.WadManager
import ar.com.scacchi.nightmare.data.asset.patch.Picture
import ar.com.scacchi.nightmare.engine.Engine
import ar.com.scacchi.nightmare.engine.GameState
import ar.com.scacchi.nightmare.ext.light
import ar.com.scacchi.nightmare.settings.H_HEIGHT
import ar.com.scacchi.nightmare.settings.H_WIDTH
import ar.com.scacchi.nightmare.settings.SCREEN_HEIGHT
import ar.com.scacchi.nightmare.settings.SCREEN_WIDTH
import ar.com.scacchi.nightmare.ui.render.map.getColor
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class User3dViewDrawScope(
    private val parentScope: DrawScope,
    private val gameState: GameState,
    private val wadManager: WadManager,
) : DrawScope by parentScope {

    var isTraverseBsp = true
    var segHandler: SegHandler = SegHandler(this, gameState.player)

    val skyTexAlt = 100
    val skyInvScale = 160 / SCREEN_HEIGHT
    val skyId = "SKY1"

    fun renderBspNode(gameState: GameState, nodeId: Int) {
        if (gameState.episodeMap.nodes.isEmpty()) return
        if (isTraverseBsp.not()) {

            if (nodeId >= BSP.SUB_SECTOR_IDENTIFIER) {
                this.renderSubSector(
                    gameState = gameState,
                    subSectorId = nodeId - BSP.SUB_SECTOR_IDENTIFIER
                )
                return
            }

            val node = gameState.episodeMap.nodes[nodeId]

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

        val subSector = gameState.episodeMap.subSectors[subSectorId]

        for (segId in 0 until subSector.segCount) {
            val seg = gameState.episodeMap.segs[subSector.firstSegId + segId]

            val result = BSP.addSegmentToFov(
                gameState.player, seg.startVertex, seg.endVertex
            ) ?: continue

//            drawVLines(engine, result.startX, result.endX, subSectorId)
            segHandler.classifySegment(
                seg,
                result.startX.toInt(),
                result.endX.toInt(),
                result.realWallAngle
            )
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

    fun drawFlat(
        texId: String,
        lightLevel: Float,
        x: Float,
        y1: Float,
        y2: Float,
        worldZ: Float,
    ) {
        if (y1 < y2) {
            if (texId == skyId) {
                val texColumn =
                    2.2f * (this.gameState.player.angle + SegHandler.xToAngleTable[x.toInt()])

                this.drawWallCol(
                    tex = wadManager.getGeneralPicture(skyId),
                    texCol = texColumn.toInt(),
                    x = x,
                    y1 = y1,
                    y2 = y2,
                    texAlt = skyTexAlt,
                    invScale = skyInvScale, lightLevel = 1.0f
                )
            } else {

                val flatTex = wadManager.getFlat(texId)

                drawFlatCol(flatTex, x, y1, y2, lightLevel, worldZ)
            }
        }
    }

    fun drawFlatCol(
        flatTex: Picture, x: Float, y1: Float, y2: Float, lightLevel: Float, worldZ: Float,
    ) {
        val playerDirX = cos(gameState.player.angle)
        val playerDirY = sin(gameState.player.angle)

        for (iy in y1.toInt() until y2.toInt()) {
            val z = H_WIDTH * worldZ / (H_HEIGHT - iy)

            val px = playerDirX * z + gameState.player.xPos
            val py = playerDirY * z + gameState.player.yPos

            val leftX = -playerDirY * z + px
            val leftY = playerDirX * z + py
            val rightX = playerDirY * z + px
            val rightY = -playerDirX * z + py

            val dx = (rightX - leftX) / SCREEN_WIDTH
            val dy = (rightY - leftY) / SCREEN_WIDTH

            val tx = (leftX + dx * x).toInt() and 63
            val ty = (leftY + dy * x).toInt() and 63

            val col = flatTex[tx, ty]
            val color =
                if (col == null) Color.Transparent
                else gameState.playPal[0][col.toInt()].light(lightLevel)
            this.drawPoints(
                points = listOf(Offset(x, iy.toFloat())),
                pointMode = PointMode.Points,
                color = color,
                strokeWidth = 1f,
                cap = StrokeCap.Square
            )
        }
    }

    fun drawWallCol(
        tex: Picture,
        texCol: Int,
        x: Float,
        y1: Float,
        y2: Float,
        texAlt: Int, // Height adjustment or vertical displacement of the texture.
        invScale: Float, // Factor for scaling texture according to resolution
        lightLevel: Float,
    ) {
        if (y1 < y2) {

            val texW = tex.width.toInt()
            val texH = tex.height.toInt()
            val texCol = texCol % texW
            var texY = texAlt + (y1 - H_HEIGHT) * invScale

            for (iy in y1.toInt() until y2.toInt()) {
                val col = tex[texCol, (texY % texH).toInt()]
                val color =
                    if (col == null) Color.Transparent
                    else gameState.playPal[0][col.toInt()].light(lightLevel)
                this.drawPoints(
                    points = listOf(Offset(x, iy.toFloat())),
                    pointMode = PointMode.Points,
                    color = color,
                    strokeWidth = 1f,
                    cap = StrokeCap.Square
                )
                texY += invScale
            }
        }
    }
}