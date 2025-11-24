package ar.com.scacchi.nightmare.settings

import androidx.compose.ui.unit.IntSize

const val DOOM_W: Int = 320
const val DOOM_H: Int = 200

val DOOM_RES: IntSize = IntSize(DOOM_W, DOOM_H)

const val SCALE: Float = 1.0f

const val WIDTH: Float = DOOM_W * SCALE
const val HEIGHT: Float = DOOM_H * SCALE

val WIN_RES: IntSize = IntSize(WIDTH.toInt(), HEIGHT.toInt())

const val FOV: Float = (Math.PI / 2).toFloat()
const val H_FOV: Float = (FOV / 2).toFloat()

const val PLAYER_SPEED: Float = 3f
const val PLAYER_ROT_SPEED: Float = 0.12f