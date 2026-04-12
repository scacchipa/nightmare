package ar.com.scacchi.nightmare.ext

import kotlin.math.PI

const val TWO_PI = (2f * Math.PI).toFloat()
const val HALF_PI = (Math.PI / 2f).toFloat()
const val THREE_HALF_PI = (Math.PI * 3f / 2f).toFloat()
const val QUARTER_PI = (Math.PI / 4f).toFloat()
const val THREE_QUARTER_PI = (Math.PI * 3f / 4f).toFloat()
const val FIVE_QUARTER_PI = (Math.PI * 5f / 4f).toFloat()
const val SEVEN_QUARTER_PI = (Math.PI * 7f / 4f).toFloat()

const val COS45 = 0.70710677f
const val TAN45 = 1f

fun Float.degreeToRadian(): Float = (this * PI / 180).toFloat()
fun Float.normalizeAngle(): Float = ((this % TWO_PI) + TWO_PI) % TWO_PI
fun Float.normalize(value: Float): Float = ((this % value) + value) % value
