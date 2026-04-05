package ar.com.scacchi.nightmare.ui.render.mainview

import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import ar.com.scacchi.nightmare.BSP
import ar.com.scacchi.nightmare.SegHandler
import ar.com.scacchi.nightmare.data.asset.DoomBitmap
import ar.com.scacchi.nightmare.engine.GameState
import ar.com.scacchi.nightmare.engine.ImageRepository
import ar.com.scacchi.nightmare.ext.light
import ar.com.scacchi.nightmare.ext.normalize
import ar.com.scacchi.nightmare.settings.H_HEIGHT
import ar.com.scacchi.nightmare.settings.H_WIDTH
import ar.com.scacchi.nightmare.settings.SCREEN_HEIGHT
import ar.com.scacchi.nightmare.settings.SCREEN_WIDTH
import ar.com.scacchi.nightmare.ui.render.map.getColor
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class Nm3DScreen(
    private val gameState: GameState,
    private val imageRepository: ImageRepository,
) {

    val nmBitmap = NmBitmap(SCREEN_WIDTH.toInt(), SCREEN_HEIGHT.toInt())
    var isTraverseBsp = true
    var segHandler: SegHandler = SegHandler(this, gameState.player, imageRepository)

    val skyTexAlt = 100
    val skyInvScale = 160 / SCREEN_HEIGHT
    val skyId = "F_SKY1"

    fun createBitmap(): Bitmap = nmBitmap.getBitmap()

    @OptIn(ExperimentalTime::class)
    fun renderBspNode(gameState: GameState, nodeId: Int) {
        val startNow = Clock.System.now()
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
                renderBspNode(gameState, node.backChildId)
                if (BSP.checkBBox(gameState.player, node.frontBoundBox)) {
                    renderBspNode(gameState, node.frondChildId)
                }
            } else {
                renderBspNode(gameState, node.frondChildId)
                if (BSP.checkBBox(gameState.player, node.backBoundBox)) {
                    renderBspNode(gameState, node.backChildId)
                }
            }
        }
        println("Time to draw a frame ${Clock.System.now() - startNow}")
    }

    fun renderSubSector(gameState: GameState, subSectorId: Int) {

        val subSector = gameState.episodeMap.subSectors[subSectorId]

        for (segId in 0 until subSector.segCount) {
            val seg = gameState.episodeMap.segs[subSector.firstSegId + segId]

            val result = BSP.addSegmentToFov(
                gameState.player, seg.startVertex, seg.endVertex
            ) ?: continue

//            drawVLines(result.startX, result.endX, subSectorId)
            segHandler.classifySegment(
                seg, result.startX.toInt(), result.endX.toInt(), result.realWallAngle
            )
        }
    }


    fun drawVLines(x1: Float, x2: Float, subSectorId: Int) {
        val color = getColor(subSectorId)

        nmBitmap.drawLine(
            color = color.toArgb(),
            x1 = x1.toInt(), y1 = 0,
            x2 = x1.toInt(), y2 = SCREEN_HEIGHT.toInt(),
        )
        nmBitmap.drawLine(
            color = color.toArgb(),
            x1 = x2.toInt(), y1 = 0,
            x2 = x2.toInt(), y2 = SCREEN_HEIGHT.toInt(),
        )
    }

    val colorMap = mutableMapOf<String, Color>()

    fun getColor(tex: String, lightLevel: Float): Color {
        val colorLabel = tex + lightLevel.toString()

        return colorMap.getOrPut(colorLabel) {
            val rnd = Random(tex.hashCode())

            Color(
                red = (rnd.nextInt(50, 256) * lightLevel).toInt(),
                green = (rnd.nextInt(50, 256) * lightLevel).toInt(),
                blue = (rnd.nextInt(50, 256) * lightLevel).toInt(),
            )
        }
    }

    fun drawFlat(
        texName: String,
        lightLevel: Float,
        x: Float,
        y1: Float,
        y2: Float,
        worldZ: Float,
    ) {
        if (y1 < y2) {
            if (texName == skyId) {
                val texColumn =
                    2.2f * (this.gameState.player.angle + SegHandler.xToAngleTable[x.toInt()]) * 90

                this.drawWallCol(
                    tex = imageRepository.getPictureDoomBitmap("SKY1"),
                    texCol = texColumn.toInt(),
                    x = x,
                    y1 = y1,
                    y2 = y2,
                    texAlt = skyTexAlt,
                    invScale = skyInvScale,
                    lightLevel = 1.0f
                )
            } else {

                val flatTex = imageRepository.getFlatDoomBitmap(texName)

                drawFlatCol(flatTex, x, y1, y2, lightLevel, worldZ)
            }
        }
    }

    fun drawFlatCol(
        flatTex: DoomBitmap, x: Float, y1: Float, y2: Float, lightLevel: Float, worldZ: Float,
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
                if (col == (-1).toShort()) Color.Transparent
                else gameState.playPal[0][col.toInt()].light(lightLevel)
            nmBitmap.drawPixel(
                x = x.toInt(),
                y = iy,
                color = color.toArgb(),
            )
        }
    }

    fun drawWallCol(
        tex: DoomBitmap,
        texCol: Int,
        x: Float,
        y1: Float,
        y2: Float,
        texAlt: Int, // Height adjustment or vertical displacement of the texture.
        invScale: Float, // Factor for scaling texture according to resolution
        lightLevel: Float,
    ) {
        if (y1 < y2) {
            val texW = tex.width
            val texH = tex.height
            var texY = texAlt + (y1 - H_HEIGHT) * invScale

            for (iy in y1.toInt() until y2.toInt()) {
                val xp = texCol.normalize(texW)
                val yp = texY.toInt().normalize(texH)
                val paletteColor = tex[xp, yp]
                val color =
                    if (paletteColor == (-1).toShort()) Color.Transparent
                    else gameState.playPal[0][paletteColor.toInt()].light(lightLevel)
                nmBitmap.drawPixel(
                    x = x.toInt(), y = iy,
                    color = color.toArgb(),
                )
                texY += invScale
            }
        }
    }
}