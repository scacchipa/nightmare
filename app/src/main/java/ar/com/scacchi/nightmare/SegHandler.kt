package ar.com.scacchi.nightmare

import ar.com.scacchi.nightmare.engine.ImageRepository
import ar.com.scacchi.nightmare.engine.Player
import ar.com.scacchi.nightmare.engine.Seg
import ar.com.scacchi.nightmare.ext.atan2
import ar.com.scacchi.nightmare.ext.hypotenuse
import ar.com.scacchi.nightmare.settings.H_HEIGHT
import ar.com.scacchi.nightmare.settings.H_WIDTH
import ar.com.scacchi.nightmare.settings.SCREEN_DIST
import ar.com.scacchi.nightmare.settings.SCREEN_HEIGHT
import ar.com.scacchi.nightmare.settings.SCREEN_WIDTH
import ar.com.scacchi.nightmare.source.wad.lump.LINEDEF_FLAGS
import ar.com.scacchi.nightmare.ui.render.mainview.Nm3DScreen
import java.util.BitSet
import kotlin.math.PI
import kotlin.math.absoluteValue
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin
import kotlin.math.tan

class SegHandler(
    val user3dScreen: Nm3DScreen,
    var player: Player,
    val imageRepository: ImageRepository,
) {
    val MAX_SCALE = 64.0.toFloat()
    val MIN_SCALE = 0.00390625.toFloat()

    lateinit var seg: Seg
    var rwAngle1: Float = 0f // first vertex of the segment
    private var upperClip = IntArray(SCREEN_WIDTH.toInt()) { 0 }
    private var lowerClip = IntArray(SCREEN_WIDTH.toInt()) { SCREEN_HEIGHT.toInt() - 1 }
    private var screenRange: BitSet = BitSet(SCREEN_WIDTH.toInt()).apply {
        set(0, SCREEN_WIDTH.toInt())
    }

    companion object {
        val xToAngleTable: FloatArray =
            FloatArray(SCREEN_WIDTH.toInt() + 1) {
                atan((H_WIDTH - it) / SCREEN_DIST)
            }
    }

    fun scaleFromGlobalAngle(x: Int, rwNormalAngle: Float, rwDistance: Float): Float {
        val xAngle = xToAngleTable[x]
        val num = SCREEN_DIST * cos(rwNormalAngle - xAngle - player.dirVector.atan2())
        val den = rwDistance * cos(xAngle)

        val scale = num / den
        return min(MAX_SCALE, max(MIN_SCALE, scale))
    }

    fun drawSolidWallRange(x1: Int, x2: Int) {
        //some aliases to shorten the fallowing code
        val seg = seg
        val frontSector = seg.frontSector
        val line = seg.lineDef
        val side = seg.lineDef.frontSideDef
        val upperClip = this.upperClip
        val lowerClip = this.lowerClip
        val screenRange = this.screenRange

        // textures
        val lowerTextureId = seg.lineDef.frontSideDef?.lowerTextureIdx ?: -1
        val middleTextureId = seg.lineDef.frontSideDef?.middleTextureIdx ?: -1
        val upperTextureId = seg.lineDef.frontSideDef?.upperTextureIdx ?: -1
        val ceilTextureName = frontSector?.ceilingTextureName
        val floorTextureName = frontSector?.floorTextureName
        val lightLevel = frontSector?.lightLevel

        val wallTexture = imageRepository.getTextureDoomBitmap(middleTextureId)


        // calculate the relative plane heights of the front sector
        val worldFrontZ1 = (frontSector?.ceilingHeight ?: 0) - player.height.toInt()
        val worldFrontZ2 = (frontSector?.floorHeight ?: 0) - player.height.toInt()


        /*
        * determine how the wall texture are vertically aligned
        */
        val middleTexAlt = (side?.offset?.y ?: 0f) +
                if (line.flags.and(LINEDEF_FLAGS["DONT_PEG_BOTTOM"] ?: 0u) != 0.toUShort()) {
                    val vTop = (frontSector?.floorHeight ?: 0) + wallTexture.height
                    vTop - player.height
                } else {
                    worldFrontZ1.toFloat()
                }

        // check which parts must be rendered
        val bDrawWall = side?.middleTextureName != "-"
        val bDrawCeil = worldFrontZ1 > 0
        val bDrawFloor = worldFrontZ2 < 0

        // calculate the scaling factors of the left and right edges of the wall range
        val hypotenuse = (player.pos - seg.startVertex.pos).hypotenuse()

        val startX: Int
        val endX: Int

        if (x1 < x2) {
            startX = x1
            endX = x2
        } else {
            startX = x2
            endX = x1
        }

        val rwNormalAngle = seg.radAngle + PI.toFloat() / 2f
        val offsetAngle = rwNormalAngle - this.rwAngle1
        val rwDistance = hypotenuse * cos(offsetAngle)
        val rwScale1 = scaleFromGlobalAngle(startX, rwNormalAngle, rwDistance)
        val scale2 = scaleFromGlobalAngle(endX, rwNormalAngle, rwDistance)
        val rwScaleStep = (scale2 - rwScale1) / (startX - endX)

        /*
        * determine how the wall textures are horizontally aligned
         */
        val rwOffset = hypotenuse * sin(offsetAngle) +
                seg.offset.toFloat() +
                (side?.offset?.x ?: 0f)

        val rwCenterAngle = rwNormalAngle - player.dirVector.atan2()

//        println("X1: $startX -> $rwScale1. X2: $endX -> $scale2.  Player angle: ${player.angle}")

        var wallY1 = H_HEIGHT - worldFrontZ1 * rwScale1
        val wallY1Step = rwScaleStep * worldFrontZ1

        var wallY2 = H_HEIGHT - worldFrontZ2 * rwScale1
        val wallY2Step = rwScaleStep * worldFrontZ2

        for (x in startX until endX) {
            val drawWallY1 = wallY1
            val drawWallY2 = wallY2

            if (bDrawCeil) {
                val cy1 = upperClip[x]
                val cy2 = min(drawWallY1.toInt(), lowerClip[x])
//                userScope.drawVLine(x, cy1, cy2, ceilTexture ?: "", lightLevel ?: 0f)
                user3dScreen.drawFlat(
                    texName = ceilTextureName ?: "",
                    lightLevel = lightLevel ?: 1f,
                    x = x.toFloat(),
                    y1 = cy1.toFloat(),
                    y2 = cy2.toFloat(),
                    worldZ = worldFrontZ1.toFloat()
                )
            }

            if (bDrawWall) {
                val wy1 = max(drawWallY1.toInt(), upperClip[x])
                val wy2 = min(drawWallY2.toInt(), lowerClip[x])

                if (wy1 < wy2) {
                    val angle: Float = rwCenterAngle - xToAngleTable[x]
                    val textureColumn = rwDistance * tan(angle) - rwOffset
                    val invScale = 1f / (rwScale1 - rwScaleStep * (x - startX))
                    user3dScreen.drawWallCol(
                        tex = wallTexture,
                        texCol = textureColumn.toInt(),
                        x = x.toFloat(),
                        y1 = wy1.toFloat(),
                        y2 = wy2.toFloat(),
                        texAlt = middleTexAlt.toInt(),
                        invScale = invScale,
                        lightLevel = 1.0f
                    )
                }
            }

            if (bDrawFloor) {
                val fy1 = max(drawWallY2.toInt(), upperClip[x])
                val fy2 = lowerClip[x]
//                userScope.drawVLine(x, fy1, fy2, floorTextureName ?: "", lightLevel ?: 0f)
                user3dScreen.drawFlat(
                    texName = floorTextureName ?: "",
                    lightLevel = 1f,
                    x = x.toFloat(),
                    y1 = fy1.toFloat(),
                    y2 = fy2.toFloat(),
                    worldZ = worldFrontZ2.toFloat()
                )
            }
            wallY1 += wallY1Step
            wallY2 += wallY2Step
        }
    }

    fun drawPortalWallRange(x1: Int, x2: Int) {
        // some aliases to shorten the following code
        val seg = this.seg
        val frontSector = seg.frontSector
        val backSector = seg.backSector
        val line = seg.lineDef
        val side = seg.lineDef.frontSideDef

        val upperClip = this.upperClip
        val lowerClip = this.lowerClip

        val wallTextureName = seg.lineDef.frontSideDef?.middleTextureName
        val ceilTextureName = frontSector?.ceilingTextureName
        val floorTextureName = frontSector?.floorTextureName

        // textures
        val upperWallTextureName = side?.upperTextureName
        val lowerWallTextureName = side?.lowerTextureName
        val upperWallTextureIdx = side?.upperTextureIdx
        val lowerWallTextureIdx = side?.lowerTextureIdx

        val texCeilName = frontSector?.ceilingTextureName
        val texFloorName = frontSector?.floorTextureName

        val lightLevel = frontSector?.lightLevel

        // calculate the relative plane heights of front and back sector
        val worldFrontZ1 = (frontSector?.ceilingHeight ?: 0) - player.height
        val worldBackZ1 = (backSector?.ceilingHeight ?: 0) - player.height
        val worldFrontZ2 = (frontSector?.floorHeight ?: 0) - player.height
        val worldBackZ2 = (backSector?.floorHeight ?: 0) - player.height

        //  check which parts must be rendered
        val bDrawUpperWall: Boolean
        val bDrawCeil: Boolean
        if (worldFrontZ1 != worldBackZ1 ||
            frontSector?.lightLevel != backSector?.lightLevel ||
            frontSector?.ceilingTextureName != backSector?.ceilingTextureName
        ) {
            bDrawUpperWall = side?.upperTextureName != "-" && worldBackZ1 < worldFrontZ1
            bDrawCeil = worldFrontZ1 >= 0 || frontSector?.ceilingTextureName == "F_SKY1"
        } else {
            bDrawUpperWall = false
            bDrawCeil = false
        }

        val bDrawLowerWall: Boolean
        val bDrawFloor: Boolean
        if (worldFrontZ2 != worldBackZ2 ||
            frontSector?.floorTextureName != backSector?.floorTextureName ||
            frontSector?.lightLevel != backSector?.lightLevel
        ) {
            bDrawLowerWall = side?.lowerTextureName != "-" && worldBackZ2 > worldFrontZ2
            bDrawFloor = worldFrontZ2 <= 0
        } else {
            bDrawLowerWall = false
            bDrawFloor = false
        }

        // if nothing must be rendered, we can skip this seg
        if (bDrawUpperWall.not() &&
            bDrawCeil.not() &&
            bDrawLowerWall.not() &&
            bDrawFloor.not()
        ) {
            return
        }

        // calculate the scaling factors of the left and right edges of the wall range
        val rwNormalAngle = seg.radAngle + PI.toFloat() / 2f
        val offsetAngle = rwNormalAngle - this.rwAngle1

        val hypotenuse = (player.pos - seg.startVertex.pos).hypotenuse()

        val rwDistance = hypotenuse * cos(offsetAngle)

        val rwScaleStep: Float
        val rwScale1 = scaleFromGlobalAngle(x1, rwNormalAngle, rwDistance)
        if (x1 < x2) {
            val scale2 = scaleFromGlobalAngle(x2, rwNormalAngle, rwDistance)
            rwScaleStep = (scale2 - rwScale1) / (x2 - x1)
        } else {
            val scale2 = scaleFromGlobalAngle(x1, rwNormalAngle, rwDistance)
            rwScaleStep = (scale2 - rwScale1) / (x2 - x1)
        }

        /*
        * determine how the wall textures are vertically aligned
        */
        val upperTexAlt: Float = if (bDrawUpperWall) {
            val upperWallTexture =
                imageRepository.getTextureDoomBitmap(side?.upperTextureIdx ?: -1)
            (side?.offset?.y ?: 0f) +
                    (if (line.flags.and(LINEDEF_FLAGS["DONT_PEG_TOP"] ?: 0u) != 0u.toUShort()) {
                        worldFrontZ1
                    } else {
                        val vTop = (backSector?.ceilingHeight ?: 0) + upperWallTexture.height
                        vTop - player.height
                    })
        } else 0f

        val lowerTexAlt = if (bDrawLowerWall) {
            (side?.offset?.y ?: 0f) +
                    if (line.flags.and(LINEDEF_FLAGS["DONT_PEG_TOP"] ?: 0u) != 0u.toUShort()) {
                        worldFrontZ1
                    } else {
                        worldBackZ2
                    }
        } else 0f
        /*
        * determine how the wall textures are horizontally aligned
         */
        val segTextured = bDrawUpperWall or bDrawLowerWall
        val rwOffset: Float =
            if (segTextured) {
                - hypotenuse * sin(offsetAngle) +
                        seg.offset.toFloat() +
                        (side?.offset?.x ?: 0f)
            } else 0f
        //
        val rwCenterAngle = - rwNormalAngle + player.dirVector.atan2()


        // the y positions of the top / bottom edges of the wall on the screen
        var wallY1 = H_HEIGHT - worldFrontZ1 * rwScale1
        val wallY1Step = -rwScaleStep * worldFrontZ1
        var wallY2 = H_HEIGHT - worldFrontZ2 * rwScale1
        val wallY2Step = -rwScaleStep * worldFrontZ2

        // the y position of the top edge of the portal
        var portalY1: Float = 0f
        var portalY1Step: Float = 0f

        if (bDrawUpperWall) {
            if (worldBackZ1 > worldFrontZ2) {
                portalY1 = H_HEIGHT - worldBackZ1 * rwScale1
                portalY1Step = -rwScaleStep * worldBackZ1
            } else {
                portalY1 = wallY2
                portalY1Step = wallY2Step
            }
        }

        var portalY2: Float = 0f
        var portalY2Step: Float = 0f
        if (bDrawLowerWall) {
            if (worldBackZ2 < worldFrontZ1) {
                portalY2 = H_HEIGHT - worldBackZ2 * rwScale1
                portalY2Step = -rwScaleStep * worldBackZ2
            } else {
                portalY2 = wallY1
                portalY2Step = wallY1Step
            }
        }

        //# now the rendering is carried out
        for (x in x1 until x2) {
            val drawWallY1 = wallY1
            val drawWallY2 = wallY2

            val angle: Float
            val textureColumn: Float
            val invScale: Float
            if (segTextured) {
                angle = rwCenterAngle + xToAngleTable[x]
                textureColumn = - (rwDistance * tan(angle) - rwOffset)
                invScale = 1f / (rwScale1 + rwScaleStep * (x - x1))
            } else {
                angle = 0f
                textureColumn = 0f
                invScale = 0f
            }

            if (bDrawUpperWall) {
                val drawUpperWallY1 = wallY1
                val drawUpperWallY2 = portalY1
                //
                if (bDrawCeil) {
                    val cy1 = upperClip[x]
                    val cy2 = min(drawWallY1.toInt(), lowerClip[x])
//                    userScope.drawVLine(x, cy1, cy2, texCeilId ?: "", lightLevel ?: 0f)
                    user3dScreen.drawFlat(
                        texName = ceilTextureName ?: "",
                        lightLevel = lightLevel ?: 1f,
                        x = x.toFloat(),
                        y1 = cy1.toFloat(),
                        y2 = cy2.toFloat(),
                        worldZ = worldFrontZ1
                    )
                }
                //
                val wy1 = max(drawUpperWallY1.toInt(), upperClip[x])
                val wy2 = min(drawUpperWallY2.toInt(), lowerClip[x])
//                userScope.drawVLine(x, wy1, wy2, upperWallTexture ?: "", lightLevel ?: 0f)
                user3dScreen.drawWallCol(
                    tex = imageRepository.getTextureDoomBitmap(upperWallTextureIdx ?: -1),
                    lightLevel = 1f,
                    x = x.toFloat(),
                    y1 = wy1.toFloat(),
                    y2 = wy2.toFloat(),
                    texCol = textureColumn.toInt(),
                    texAlt = upperTexAlt.toInt(),
                    invScale = invScale,
                )

                //
                if (upperClip[x] < wy2) {
                    upperClip[x] = wy2
                }
                //
                portalY1 += portalY1Step
            }

            if (bDrawCeil) {
                val cy1 = upperClip[x]
                val cy2 = min(drawWallY1.toInt(), lowerClip[x])
//                userScope.drawVLine(x, cy1, cy2, texCeilId ?: "", lightLevel ?: 0f)
                user3dScreen.drawFlat(
                    texName = ceilTextureName ?: "",
                    lightLevel = lightLevel ?: 1f,
                    x = x.toFloat(),
                    y1 = cy1.toFloat(),
                    y2 = cy2.toFloat(),
                    worldZ = worldFrontZ1
                )
                //
                if (upperClip[x] < cy2) {
                    upperClip[x] = cy2
                }
            }

            if (bDrawLowerWall) {
                //
                if (bDrawFloor) {
                    val fy1 = max(drawWallY2.toInt(), upperClip[x])
                    val fy2 = lowerClip[x]
//                    userScope.drawVLine(x, fy1, fy2, texFloorName ?: "", lightLevel ?: 0f)
                    user3dScreen.drawFlat(
                        texName = texFloorName ?: "",
                        lightLevel = 1f,
                        x = x.toFloat(),
                        y1 = fy1.toFloat(),
                        y2 = fy2.toFloat(),
                        worldZ = worldFrontZ2
                    )
                }
                //
                val drawLowerWallY1 = portalY2
                val drawLowerWallY2 = wallY2
                //
                val wy1 = max(drawLowerWallY1.toInt(), upperClip[x])
                val wy2 = min(drawLowerWallY2.toInt(), lowerClip[x])
//                userScope.drawVLine(x, wy1, wy2, lowerWallTextureName ?: "", lightLevel ?: 0f)
                user3dScreen.drawWallCol(
                    tex = imageRepository.getTextureDoomBitmap(lowerWallTextureIdx ?: -1),
                    texCol = textureColumn.toInt(),
                    x = x.toFloat(),
                    y1 = wy1.toFloat(),
                    y2 = wy2.toFloat(),
                    texAlt = lowerTexAlt.toInt(),
                    invScale = invScale,
                    lightLevel = 1f
                )

                //
                if (lowerClip[x] > wy1) {
                    lowerClip[x] = wy1
                }
                //
                portalY2 += portalY2Step
            }

            if (bDrawFloor) {
                val fy1 = max(drawWallY2.toInt(), upperClip[x])
                val fy2 = lowerClip[x]
//                userScope.drawVLine(x, fy1, fy2, texFloorName ?: "", lightLevel ?: 0f)

                user3dScreen.drawFlat(
                    texFloorName ?: "",
                    lightLevel = 1f,
                    x = x.toFloat(),
                    y1 = fy1.toFloat(),
                    y2 = fy2.toFloat(),
                    worldZ = worldFrontZ2
                )

                //
                if (lowerClip[x] > drawWallY2) {
                    lowerClip[x] = fy1
                }
            }

            wallY1 += wallY1Step
            wallY2 += wallY2Step
        }
    }

    fun clipPortalWalls(xStart: Int, xEnd: Int) {
        // 1. Crea el rango de la pared actual
        val currWall = BitSet(xEnd).apply {
            set(min(xStart, xEnd), max(xEnd, xStart))
        }

        // 2. Intersección: ¿Qué partes de la pared ven espacio vacio?)
        // Usamos clone para no modificar el screenRange global
        val intersection = (currWall.clone() as BitSet).apply {
            and(screenRange)
        }

        if (intersection.isEmpty.not()) {
            val intersectionSize = intersection.cardinality()
            val wallSize = (xEnd - xStart).absoluteValue

            if (intersectionSize == wallSize) {
                // Caso A: La pared del portal es totalmente visible
                drawPortalWallRange(xStart, xEnd)
            } else {
                // Caso B: La pared está fragmentada por obstáculos previos
                val x = intersection.nextSetBit(0)

                // Recorremos los bit encendidos para enontrar segmentos contínuos
                var currentStart = x
                var i = x

                while (i < xEnd) {
                    val nextEmpty = intersection.nextClearBit(i)

                    // Dibujamos el segmento visible encontrado
                    drawPortalWallRange(currentStart, nextEmpty)

                    // Buscamos el inicio del siguiente segmento visible
                    val nextVisible = intersection.nextSetBit(nextEmpty)
                    if (nextVisible == -1 || nextVisible > xEnd) break

                    currentStart = nextVisible
                    i = nextVisible
                }
            }
            // Nota: a diferencia del clipSolidWalls, aquí. No hacemos
            // screenRange.andNot(intersection) porque es un portal.
        }
    }

    fun clipSolidWalls(xStart: Int, xEnd: Int) {

        // 1. Verificar si la pantalla está totalmente llena
        if (this.screenRange.isEmpty.not()) {

            // Creamos un BitSet temporal para la pared actual (rango xStart) hasta xEnd)
            val currWall = BitSet(max(xStart, xEnd)).apply {
                set(min(xStart, xEnd), max(xStart, xEnd))
            }

            // Intersección: qué parte de la pared cae en espacio vacio.
            val intersection = (currWall.clone() as BitSet).apply {
                and(screenRange)
            }

            if (intersection.isEmpty.not()) {
                if (intersection.cardinality() == (xEnd - xStart).absoluteValue) {
                    // Caso A: la pared es totalmente visible (sin cortes)
                    drawSolidWallRange(xStart, xEnd)
                } else {
                    // Case B: La pared está fragmentada estilo sorted + zip)
                    var x = intersection.nextSetBit(0)

                    var x1 = x

                    while (x1 != -1) {
                        val nextEmpty = intersection.nextClearBit(x1)
                        // dibujamos el segmento continuo encontrado

                        drawSolidWallRange(x, nextEmpty)

                        // Buscamos el inicio del siguiente fragmento visible
                        val x2 = intersection.nextSetBit(nextEmpty)
                        if (x2 == -1 || x2 >= xEnd) break

                        x = x2
                        x1 = x2
                    }
                }
                // eliminamos los bits recién dibujados de la pantalla disponible
                screenRange.andNot(intersection)
            }
        } else {
            // pantalla llena: detener el recorrid del BSP
            this.user3dScreen.isTraverseBsp = false
        }
    }

    fun classifySegment(segment: Seg, x1: Int, x2: Int, rwAngle1: Float) {

        // Guardamos los datos actuales del segmento en la clase
        this.seg = segment
        this.rwAngle1 = rwAngle1

        // 1. ¿No cruza ni un solo píxel?
        if (x1 == x2) return

        val backSector = segment.backSector
        val frontSector = segment.frontSector

        // 2. Manejo de paredes sólidas (Si no hay sector trasero, es una pared impasable)
        if (backSector == null) {
            clipSolidWalls(x1, x2)
            return
        }

        // 3. Pared con ventana (Portal)
        // Si las alturas de techo o suelo son diferentes, es una abertura que requiere clipping de portal
        if (frontSector?.ceilingHeight != backSector.ceilingHeight ||
            frontSector.floorHeight != backSector.floorHeight
        ) {
            clipPortalWalls(x1, x2)
            return
        }

        // 4. Rechazar líneas invisibles (Triggers o eventos especiales)
        // Si tienen texturas iguales, misma luz y no hay textura media, no se dibuja nada.
        val frontSideDef = segment.lineDef.frontSideDef
        if (backSector.ceilingTextureName == frontSector.ceilingTextureName &&
            backSector.floorTextureName == frontSector.floorTextureName &&
            backSector.lightLevel == frontSector.lightLevel &&
            frontSideDef?.middleTextureName == "-"
        ) {
            return
        }

        // 5. Fronteras con diferentes niveles de luz o texturas
        // Si llegó aquí, es una línea divisoria que necesita procesarse como portal
        clipPortalWalls(x1, x2)
    }
}
