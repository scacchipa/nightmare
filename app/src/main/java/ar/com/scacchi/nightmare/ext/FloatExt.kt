package ar.com.scacchi.nightmare.ext

import ar.com.scacchi.nightmare.BSP.Companion.TWO_PI
import kotlin.math.PI

fun Float.degreeToRadian(): Float = (this * PI / 180).toFloat()
fun Float.normalizeAngle(): Float = ((this % TWO_PI) + TWO_PI) % TWO_PI
fun Float.normalize(value: Float): Float = ((this % value) + value) % value
