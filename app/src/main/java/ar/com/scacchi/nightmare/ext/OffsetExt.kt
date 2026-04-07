package ar.com.scacchi.nightmare.ext

import androidx.compose.ui.geometry.Offset
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

fun Offset.rotateBy(angle: Float): Offset {
    val cos = cos(angle)
    val sin = sin(angle)
    return Offset(
        x = x * cos - y * sin,
        y = x * sin + y * cos,
    )
}

fun Offset.Companion.scalar(rad: Float): Offset = Offset(cos(rad), sin(rad))

fun Offset.hypotenuse(): Float = hypot(x, y)
