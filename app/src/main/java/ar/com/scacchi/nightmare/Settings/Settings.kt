package ar.com.scacchi.nightmare.Settings

import androidx.compose.ui.unit.IntSize

const val DOOM_W = 320
const val DOOM_H = 200

val DOOM_RES = IntSize(DOOM_W, DOOM_H)

const val SCALE = 5.0

const val WIDTH = DOOM_W * SCALE
const val HEIGHT = DOOM_H * SCALE

val WIN_RES = IntSize(WIDTH.toInt(), HEIGHT.toInt())