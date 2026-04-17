package ar.com.scacchi.nightmare.settings

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import ar.com.scacchi.nightmare.ext.versorWithAngle
import kotlin.math.tan

const val DOOM_W: Int = 320
const val DOOM_H: Int = 200

val DOOM_RES: IntSize = IntSize(DOOM_W, DOOM_H)

const val SCALE: Float = 2.5f

const val SCREEN_WIDTH: Float = DOOM_W * SCALE
const val SCREEN_HEIGHT: Float = DOOM_H * SCALE
const val SCREEN_ASPECT: Float = SCREEN_WIDTH / SCREEN_HEIGHT

val WIN_RES: IntSize = IntSize(SCREEN_WIDTH.toInt(), SCREEN_HEIGHT.toInt())

const val FOV: Float = (Math.PI / 2).toFloat()
const val H_FOV: Float = (FOV / 2)
val leftLimitFOVVersor: Offset = Offset.versorWithAngle(H_FOV)
val rightLimitFOVVersor: Offset = Offset.versorWithAngle(-H_FOV)
val fovVersor: Offset = Offset.versorWithAngle(FOV)

const val PLAYER_SPEED: Float = 3f
const val PLAYER_ROT_SPEED: Float = 0.12f
const val PLAYER_HEIGHT: Float = 41f

const val H_WIDTH = SCREEN_WIDTH / 2
const val H_HEIGHT = SCREEN_HEIGHT / 2
val SCREEN_DIST = H_WIDTH / tan(H_FOV)